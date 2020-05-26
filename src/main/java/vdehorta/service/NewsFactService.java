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
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static vdehorta.security.RoleEnum.CONTRIBUTOR;
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
    private final AuthenticationService authenticationService;

    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper,
                           UserService userService,
                           NewsCategoryService newsCategoryService,
                           ClockService clockService,
                           VideoFileService videoFileService,
                           AuthenticationService authenticationService) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
        this.userService = userService;
        this.newsCategoryService = newsCategoryService;
        this.clockService = clockService;
        this.videoFileService = videoFileService;
        this.authenticationService = authenticationService;
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

    public Page<NewsFactDetailDto> getMyNewsFacts(Pageable pageable) throws AuthenticationRequiredException {
        log.debug("Getting news facts of connected user");

        authenticationService.assertCurrentUserHasRole(CONTRIBUTOR);
        String loggedUser = authenticationService.getCurrentUserLoginOrNull();
        return newsFactRepository.findAllByOwner(pageable, loggedUser).map(newsFactMapper::newsFactToNewsFactDetailDto);
    }

    public NewsFactDetailDto create(NewsFactDetailDto newsFactDetailDto, MultipartFile videoFile) throws AuthenticationRequiredException {
        log.debug("Creating  news fact...");

        authenticationService.assertCurrentUserHasRole(CONTRIBUTOR);

        String currentUserLogin = authenticationService.getCurrentUserLoginOrNull();

        NewsFact newsFact = newsFactMapper.newsFactDetailDtoToNewsFact(newsFactDetailDto);

        String videoFileRef = videoFileService.save(videoFile);

        newsFact.setVideoPath(videoFileRef);
        newsFact.setOwner(currentUserLogin);
        newsFact.setNewsCategoryLabel(newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId()).getLabel());

        LocalDateTime now = clockService.now();
        newsFact.setCreatedDate(now);
        newsFact.setLastModifiedDate(now);
        NewsFact createdNewsFact = this.newsFactRepository.save(newsFact);
        log.debug("Created Information for News Fact: {}", createdNewsFact);

        return newsFactMapper.newsFactToNewsFactDetailDto(createdNewsFact);
    }

    public NewsFactDetailDto update(NewsFactDetailDto newsFactDetailDto) {
        log.debug("Updating news fact");

        authenticationService.assertCurrentUserHasRole(CONTRIBUTOR);
        String currentUserLogin = authenticationService.getCurrentUserLoginOrNull();

        String id = newsFactDetailDto.getId();
        Preconditions.checkNotNull(id, "News fact id is null!");

        NewsCategoryDto newNewsCategory = newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId());

        LocalDateTime newEventDate = LocalDate.parse(newsFactDetailDto.getEventDate(), LOCAL_DATE_FORMATTER).atStartOfDay();

        NewsFact toUpdateNewsFact = newsFactRepository.findById(id).orElseThrow(() -> new WrongNewsFactIdException(id));
        if (!toUpdateNewsFact.getOwner().equals(currentUserLogin)) {
            throw new WrongNewsFactIdException(id);
        }

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

    public void delete(String newsFactId) throws AuthenticationRequiredException {
        log.debug("Deleting news fact  with id {}", newsFactId);

        authenticationService.assertCurrentUserHasRole(CONTRIBUTOR);
        String currentUserLogin = authenticationService.getCurrentUserLoginOrNull();

        NewsFact newsFact = newsFactRepository.findById(newsFactId).orElseThrow(() -> new WrongNewsFactIdException(newsFactId));
        if (!newsFact.getOwner().equals(currentUserLogin)) {
            throw new WrongNewsFactIdException(newsFactId);
        }

        newsFactRepository.deleteById(newsFactId);
    }
}
