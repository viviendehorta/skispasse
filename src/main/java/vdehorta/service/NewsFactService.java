package vdehorta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.UnexistingLoginException;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

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

    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper,
                           UserService userService) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
        this.userService = userService;
    }

    public List<NewsFactNoDetailDto> getAll() {
        return newsFactMapper.newsFactsToNewsFactNoDetailDtos(newsFactRepository.findAll());
    }

    public NewsFactDetailDto getById(String id) {
        log.debug("Getting news fact  with id {}", id);
        Optional<NewsFact> newsFactOptional = newsFactRepository.findById(id);
        NewsFact newsFact = newsFactOptional.orElseThrow(() -> new WrongNewsFactIdException());
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFact);
    }

    public Page<NewsFactDetailDto> getByOwner(Pageable pageable, String ownerLogin) {

        //Check that user with given login exists, throws exception if it doesn't
        userService.getUserWithAuthoritiesByLogin(ownerLogin).orElseThrow(() -> new UnexistingLoginException());

        return newsFactRepository.findAllByOwner(pageable, ownerLogin).map(newsFactMapper::newsFactToNewsFactDetailDto);
    }
}
