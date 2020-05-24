package vdehorta.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vdehorta.config.Constants;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.security.SecurityUtils;
import vdehorta.service.errors.UnexistingLoginException;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static vdehorta.service.util.DateUtil.LOCAL_DATE_FORMATTER;

/**
 * Service class for managing news facts.
 */
@Service
public class NewsFactService {

    private final Logger log = LoggerFactory.getLogger(NewsFactService.class);

    private final NewsFactRepository newsFactRepository;
    private final NewsFactMapper newsFactMapper;
    private final UserService userService;
    private final NewsCategoryService newsCategoryService;
    private final ClockService clockService;
    private final VideoFileService videoFileService;

    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper,
                           UserService userService,
                           NewsCategoryService newsCategoryService,
                           ClockService clockService,
                           VideoFileService videoFileService) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
        this.userService = userService;
        this.newsCategoryService = newsCategoryService;
        this.clockService = clockService;
        this.videoFileService = videoFileService;
    }

    public List<NewsFactNoDetailDto> getAll() {
        log.debug("Getting all news facts...");
        return newsFactMapper.newsFactsToNewsFactNoDetailDtos(newsFactRepository.findAll());
    }

    public NewsFactDetailDto getById(String id) {
        log.debug("Getting news fact  with id {}", id);
        Optional<NewsFact> newsFactOptional = newsFactRepository.findById(id);
        NewsFact newsFact = newsFactOptional.orElseThrow(() -> new WrongNewsFactIdException(id));
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFact);
    }

    public Page<NewsFactDetailDto> getByOwner(Pageable pageable, String ownerLogin) {
        log.debug("Getting news facts owned by user {}", ownerLogin);
        //Check that user with given login exists, throws exception if it doesn't
        userService.getUserWithAuthoritiesByLogin(ownerLogin).orElseThrow(UnexistingLoginException::new);
        return newsFactRepository.findAllByOwner(pageable, ownerLogin).map(newsFactMapper::newsFactToNewsFactDetailDto);
    }

    public NewsFactDetailDto create(NewsFactDetailDto newsFactDetailDto, MultipartFile videoFile) {
        log.debug("Creating  news fact...");

        NewsFact newsFact = newsFactMapper.newsFactDetailDtoToNewsFact(newsFactDetailDto);

        String videoFileRef = videoFileService.save(videoFile);
        newsFact.setVideoPath(videoFileRef);

        /* TODO replace by SecurityUtils.getCurrentUserLoginOrThrowError() mais changer le code
             de migration qui n'est pas authentifié d'abord */
        newsFact.setOwner(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
        newsFact.setNewsCategoryLabel(newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId()).getLabel());

        LocalDateTime now = clockService.now();
        newsFact.setCreatedDate(now);
        newsFact.setLastModifiedDate(now);
        NewsFact createdNewsFact = this.newsFactRepository.save(newsFact);
        log.debug("Created Information for News Fact: {}", createdNewsFact);

        return newsFactMapper.newsFactToNewsFactDetailDto(createdNewsFact);
    }

    public void delete(String newsFactId) {
        log.debug("Deleting news fact  with id {}", newsFactId);
        newsFactRepository.deleteById(newsFactId);
    }

    public NewsFactDetailDto update(NewsFactDetailDto newsFactDetailDto) {

        //Validation
        String id = newsFactDetailDto.getId();
        Preconditions.checkNotNull(id, "News fact id is null!");
        NewsCategoryDto newNewsCategory = newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId());// Validate news category
        LocalDateTime newEventDate = LocalDate.parse(newsFactDetailDto.getEventDate(), LOCAL_DATE_FORMATTER).atStartOfDay(); //Validate event date format

        NewsFact toUpdateNewsFact = newsFactRepository.findById(id).orElseThrow(() -> new WrongNewsFactIdException(id));

        //Update
        toUpdateNewsFact.setAddress(newsFactDetailDto.getAddress());
        toUpdateNewsFact.setEventDate(newEventDate);
        toUpdateNewsFact.setLocationCoordinateX(newsFactDetailDto.getLocationCoordinate().getX());
        toUpdateNewsFact.setLocationCoordinateY(newsFactDetailDto.getLocationCoordinate().getY());
        toUpdateNewsFact.setNewsCategoryId(newNewsCategory.getId());
        toUpdateNewsFact.setNewsCategoryLabel(newNewsCategory.getLabel());
        toUpdateNewsFact.setCity(newsFactDetailDto.getCity());
        toUpdateNewsFact.setCountry(newsFactDetailDto.getCountry());
        toUpdateNewsFact.setLastModifiedDate(clockService.now());
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFactRepository.save(toUpdateNewsFact));
}
}
