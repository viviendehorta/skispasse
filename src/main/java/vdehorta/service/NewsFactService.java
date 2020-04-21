package vdehorta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.UnexistingNewsFactException;
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

    public NewsFactService(NewsFactRepository newsFactRepository,
                           NewsFactMapper newsFactMapper) {
        this.newsFactRepository = newsFactRepository;
        this.newsFactMapper = newsFactMapper;
    }

    public List<NewsFactNoDetailDto> getAll() {
        return null;
    }

    public NewsFactDetailDto getById(Long id) throws UnexistingNewsFactException {
        log.debug("Getting news fact  with id {}", id);
        Optional<NewsFact> newsFactOptional = newsFactRepository.findById(id);
        NewsFact newsFact = newsFactOptional.orElseThrow(() -> new UnexistingNewsFactException(id));
        return newsFactMapper.newsFactToNewsFactDetailDto(newsFact);
    }

    public List<NewsFactDetailDto> getByUser(String userLogin) {
        return newsFactMapper.newsFactsToNewsFactDetailDtos(newsFactRepository.findAllByCreatedBy(userLogin));
    }
}
