package vdehorta.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vdehorta.bean.InMemoryFile;
import vdehorta.bean.dto.NewsCategoryDto;
import vdehorta.bean.dto.NewsFactDetailDto;
import vdehorta.bean.dto.NewsFactNoDetailDto;
import vdehorta.domain.Media;
import vdehorta.domain.NewsFact;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.*;
import vdehorta.service.mapper.NewsFactMapper;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static vdehorta.service.util.DateUtil.DATE_FORMATTER;

/**
 * Service class for managing news facts.
 */
@Service
public class NewsFactService {

    private final Logger log = LoggerFactory.getLogger(NewsFactService.class);

    private final NewsFactRepository newsFactRepository;
    private final NewsFactMapper newsFactMapper;
    private final NewsCategoryService newsCategoryService;
    private final ClockService clockService;
    private final MediaService mediaService;


    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper,
                           NewsCategoryService newsCategoryService,
                           ClockService clockService,
                           MediaService mediaService) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
        this.newsCategoryService = newsCategoryService;
        this.clockService = clockService;
        this.mediaService = mediaService;
    }

    public List<NewsFactNoDetailDto> getAll() {
        log.debug("Getting all news facts");
        return newsFactMapper.newsFactsToNewsFactNoDetailDtos(newsFactRepository.findAll());
    }

    public NewsFactDetailDto getById(String id) throws NewsFactNotFoundException {
        log.debug("Getting news fact  by id");
        NewsFact newsFact = newsFactRepository.findById(id).orElseThrow(() -> new NewsFactNotFoundException(id));
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFact);
    }

    public Page<NewsFactDetailDto> getUserNewsFacts(String userLogin, Pageable pageable) throws AuthenticationRequiredException {
        log.debug("Getting news facts of connected user");
        return newsFactRepository.findAllByOwner(pageable, userLogin).map(newsFactMapper::newsFactToNewsFactDetailDto);
    }

    @Transactional
    public NewsFactDetailDto create(NewsFactDetailDto newsFactDetailDto, InMemoryFile inMemoryMediaFile, String creatorLogin) {
        log.debug("Creating  news fact...");

        //Force field values that should be null on creation
        newsFactDetailDto.setCreatedDate(null);
        newsFactDetailDto.setId(null);
        newsFactDetailDto.setMedia(null);
        newsFactDetailDto.setNewsCategoryLabel(null);

        NewsFact newsFact = newsFactMapper.newsFactDetailDtoToNewsFact(newsFactDetailDto);

        newsFact.setOwner(creatorLogin);
        newsFact.setNewsCategoryLabel(newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId()).getLabel());
        LocalDateTime now = clockService.now();
        newsFact.setCreatedDate(now);
        newsFact.setLastModifiedDate(now);
        NewsFact createdNewsFact = this.newsFactRepository.save(newsFact);

        /* Save media file in the end because Mongo transaction doesnt apply to GridFs so file will not be deleted if error occurs */
        Media persistedMedia = mediaService.saveMediaFile(inMemoryMediaFile, creatorLogin);

        //Update reference to video file in news fact
        createdNewsFact.setMediaId(persistedMedia.getId());
        createdNewsFact.setMediaType(persistedMedia.getMediaType());
        createdNewsFact.setMediaContentType(persistedMedia.getContentType());
        this.newsFactRepository.save(newsFact);

        log.debug("Created Information for News Fact: {}", createdNewsFact);

        return newsFactMapper.newsFactToNewsFactDetailDto(createdNewsFact);
    }

    public NewsFactDetailDto update(NewsFactDetailDto newsFactDetailDto, String updaterLogin) throws NewsFactNotFoundException {
        log.debug("Updating news fact");

        String id = newsFactDetailDto.getId();
        Preconditions.checkNotNull(id, "News fact id is null!");

        NewsCategoryDto newNewsCategory = newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId());

        LocalDateTime newEventDate = LocalDate.parse(newsFactDetailDto.getEventDate(), DATE_FORMATTER).atStartOfDay();

        NewsFact toUpdate = newsFactRepository.findById(id).orElseThrow(() -> new NewsFactNotFoundException(id));
        if (!toUpdate.getOwner().equals(updaterLogin)) {
            throw new NewsFactAccessForbiddenException(id);
        }

        //Update
        toUpdate.setAddressDetail(newsFactDetailDto.getAddressDetail());
        toUpdate.setEventDate(newEventDate);
        toUpdate.setLocationLatitude(newsFactDetailDto.getLocationCoordinate().getLatitude());
        toUpdate.setLocationLongitude(newsFactDetailDto.getLocationCoordinate().getLongitude());
        toUpdate.setNewsCategoryId(newNewsCategory.getId());
        toUpdate.setNewsCategoryLabel(newNewsCategory.getLabel());
        toUpdate.setCity(newsFactDetailDto.getCity());
        toUpdate.setCountry(newsFactDetailDto.getCountry());
        toUpdate.setLastModifiedDate(clockService.now());

        NewsFact updated = newsFactRepository.save(toUpdate);

        return newsFactMapper.newsFactToNewsFactDetailDto(updated);
    }

    @Transactional
    public void delete(String newsFactId, String deleterLogin) throws NewsFactNotFoundException {
        log.debug("Deleting news fact  with id {}", newsFactId);

        NewsFact newsFact = newsFactRepository.findById(newsFactId).orElseThrow(() -> new NewsFactNotFoundException(newsFactId));
        if (!newsFact.getOwner().equals(deleterLogin)) {
            throw new NewsFactNotFoundException(newsFactId);
        }

        newsFactRepository.deleteById(newsFactId);
        mediaService.delete(newsFact.getMediaId());
    }

    /**
     * Get an input stream to the media associated to the news fact with the given id
     *
     * @param newsFactId id of the news fact associated with the media to retrieve
     * @return An input stream to the media associated to the news fact with the given id
     */
    public InputStream getNewsFactMediaStream(String newsFactId) {
        NewsFact newsFact = newsFactRepository.findById(newsFactId).orElseThrow(() -> new NewsFactNotFoundException(newsFactId));
        String mediaId = newsFact.getMediaId();
        try {
            return this.mediaService.getMediaStream(mediaId);
        } catch (MediaNotFoundException nfe) {
            throw new NewsFactMediaNotFoundException(newsFactId, mediaId);
        } catch (MediaAccessException ae) {
            throw new NewsFactMediaAccessException(newsFactId, mediaId);
        }
    }
}
