package vdehorta.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.AuthenticationRequiredException;
import vdehorta.service.errors.NewsFactNotFoundException;
import vdehorta.service.errors.NewsFactAccessForbiddenException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static vdehorta.service.util.DateUtil.LOCAL_DATE_FORMATTER;

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
    private final VideoFileService videoFileService;

    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper,
                           NewsCategoryService newsCategoryService,
                           ClockService clockService,
                           VideoFileService videoFileService) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
        this.newsCategoryService = newsCategoryService;
        this.clockService = clockService;
        this.videoFileService = videoFileService;
    }

    public List<NewsFactNoDetailDto> getAll() {
        log.debug("Getting all news facts");
        return newsFactMapper.newsFactsToNewsFactNoDetailDtos(newsFactRepository.findAll());
    }

    public NewsFactDetailDto getById(String id) {
        log.debug("Getting news fact  by id");
        NewsFact newsFact = newsFactRepository.findById(id).orElseThrow(() -> new NewsFactNotFoundException(id));
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFact);
    }

    public Page<NewsFactDetailDto> getUserNewsFacts(String userLogin, Pageable pageable) throws AuthenticationRequiredException {
        log.debug("Getting news facts of connected user");
        return newsFactRepository.findAllByOwner(pageable, userLogin).map(newsFactMapper::newsFactToNewsFactDetailDto);
    }

    //TODO besoin d'implémenter une transaction pour la sauvegarde de news fact + fichier
    public NewsFactDetailDto create(NewsFactDetailDto newsFactDetailDto, MultipartFile videoFile, String creatorLogin) {
        log.debug("Creating  news fact...");

        NewsFact newsFact = newsFactMapper.newsFactDetailDtoToNewsFact(newsFactDetailDto);

        String videoFileRef = videoFileService.save(videoFile, creatorLogin);

        newsFact.setVideoPath(videoFileRef);
        newsFact.setOwner(creatorLogin);
        newsFact.setNewsCategoryLabel(newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId()).getLabel());

        LocalDateTime now = clockService.now();
        newsFact.setCreatedDate(now);
        newsFact.setLastModifiedDate(now);
        NewsFact createdNewsFact = this.newsFactRepository.save(newsFact);
        log.debug("Created Information for News Fact: {}", createdNewsFact);

        return newsFactMapper.newsFactToNewsFactDetailDto(createdNewsFact);
    }

    public NewsFactDetailDto update(NewsFactDetailDto newsFactDetailDto, String updaterLogin) throws NewsFactNotFoundException {
        log.debug("Updating news fact");

        String id = newsFactDetailDto.getId();
        Preconditions.checkNotNull(id, "News fact id is null!");

        NewsCategoryDto newNewsCategory = newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId());

        LocalDateTime newEventDate = LocalDate.parse(newsFactDetailDto.getEventDate(), LOCAL_DATE_FORMATTER).atStartOfDay();

        NewsFact toUpdate = newsFactRepository.findById(id).orElseThrow(() -> new NewsFactNotFoundException(id));
        if (!toUpdate.getOwner().equals(updaterLogin)) {
            throw new NewsFactAccessForbiddenException(id);
        }

        //Update
        toUpdate.setAddress(newsFactDetailDto.getAddress());
        toUpdate.setEventDate(newEventDate);
        toUpdate.setLocationCoordinateX(newsFactDetailDto.getLocationCoordinate().getX());
        toUpdate.setLocationCoordinateY(newsFactDetailDto.getLocationCoordinate().getY());
        toUpdate.setNewsCategoryId(newNewsCategory.getId());
        toUpdate.setNewsCategoryLabel(newNewsCategory.getLabel());
        toUpdate.setCity(newsFactDetailDto.getCity());
        toUpdate.setCountry(newsFactDetailDto.getCountry());
        toUpdate.setLastModifiedDate(clockService.now());

        NewsFact updated = newsFactRepository.save(toUpdate);

        return newsFactMapper.newsFactToNewsFactDetailDto(updated);
    }

    public void delete(String newsFactId, String deleterLogin) throws NewsFactNotFoundException {
        log.debug("Deleting news fact  with id {}", newsFactId);

        NewsFact newsFact = newsFactRepository.findById(newsFactId).orElseThrow(() -> new NewsFactNotFoundException(newsFactId));
        if (!newsFact.getOwner().equals(deleterLogin)) {
            throw new NewsFactNotFoundException(newsFactId);
        }

        newsFactRepository.deleteById(newsFactId);
    }
}
