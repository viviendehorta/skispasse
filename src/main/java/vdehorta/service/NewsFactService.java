package vdehorta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vdehorta.config.Constants;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.security.SecurityUtils;
import vdehorta.service.errors.UnexistingLoginException;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper,
                           UserService userService,
                           NewsCategoryService newsCategoryService,
                           ClockService clockService) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
        this.userService = userService;
        this.newsCategoryService = newsCategoryService;
        this.clockService = clockService;
    }

    public List<NewsFactNoDetailDto> getAll() {
        return newsFactMapper.newsFactsToNewsFactNoDetailDtos(newsFactRepository.findAll());
    }

    public NewsFactDetailDto getById(String id) {
        log.debug("Getting news fact  with id {}", id);
        Optional<NewsFact> newsFactOptional = newsFactRepository.findById(id);
        NewsFact newsFact = newsFactOptional.orElseThrow(WrongNewsFactIdException::new);
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFact);
    }

    public Page<NewsFactDetailDto> getByOwner(Pageable pageable, String ownerLogin) {
        //Check that user with given login exists, throws exception if it doesn't
        userService.getUserWithAuthoritiesByLogin(ownerLogin).orElseThrow(UnexistingLoginException::new);
        return newsFactRepository.findAllByOwner(pageable, ownerLogin).map(newsFactMapper::newsFactToNewsFactDetailDto);
    }

    public NewsFactDetailDto create(NewsFactDetailDto newsFactDetailDto) {
        log.debug("Creating  news fact...");
        NewsFact newsFact = newsFactMapper.newsFactDetailDtoToNewsFact(newsFactDetailDto);
        newsFact.setOwner(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
        newsFact.setNewsCategoryLabel(newsCategoryService.getById(newsFactDetailDto.getNewsCategoryId()).getLabel());
        Instant now = clockService.now();
        newsFact.setCreatedDate(now);
        newsFact.setLastModifiedDate(now);
        NewsFact createdNewsFact = this.newsFactRepository.save(newsFact);
        log.debug("Created Information for News Fact: {}", createdNewsFact);
        return newsFactMapper.newsFactToNewsFactDetailDto(createdNewsFact);
    }
}
