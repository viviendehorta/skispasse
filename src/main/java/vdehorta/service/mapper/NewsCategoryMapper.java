package vdehorta.service.mapper;

import org.springframework.stereotype.Service;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link NewsFact} and its DTO called {@link NewsFact}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class NewsCategoryMapper {

    public NewsCategoryDto newsCategoryToNewsCategoryDto(NewsCategory newsCategory) {
        NewsCategoryDto.Builder builder = new NewsCategoryDto.Builder();
        builder
            .id(newsCategory.getId())
            .label(newsCategory.getLabel());
        return builder.build();
    }

    public List<NewsCategoryDto> newsCategoriesToNewsCategoryDtos(List<NewsCategory> newsCategories) {
        return newsCategories.stream()
            .map(this::newsCategoryToNewsCategoryDto)
            .collect(Collectors.toList());
    }
}
