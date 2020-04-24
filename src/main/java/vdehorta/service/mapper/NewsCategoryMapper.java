package vdehorta.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Service
public interface NewsCategoryMapper {

    NewsCategoryDto newsCategoryToNewsCategoryDto(NewsCategory newsCategory);

    List<NewsCategoryDto> newsCategoriesToNewsCategoryDtos(List<NewsCategory> newsCategories);
}
