package vdehorta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsCategory;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.service.errors.WrongNewsCategoryIdException;
import vdehorta.service.mapper.NewsCategoryMapper;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing news facts.
 */
@Service
public class NewsCategoryService {

    private final Logger log = LoggerFactory.getLogger(NewsCategoryService.class);

    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsCategoryMapper newsCategoryMapper;

    public NewsCategoryService(NewsCategoryRepository newsCategoryRepository,
                               NewsCategoryMapper newsCategoryMapper) {
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsCategoryMapper = newsCategoryMapper;
    }

    public List<NewsCategoryDto> getAll() {
        log.debug("Getting all news categories");
        return newsCategoryMapper.newsCategoriesToNewsCategoryDtos(newsCategoryRepository.findAll());
    }

    public NewsCategoryDto getById(String id) {
        log.debug("Getting news category  with id {}", id);
        Optional<NewsCategory> newsCategoryOptional = newsCategoryRepository.findById(id);
        NewsCategory newsCategory = newsCategoryOptional.orElseThrow(() -> new WrongNewsCategoryIdException());
        return newsCategoryMapper.newsCategoryToNewsCategoryDto(newsCategory);
    }
}
