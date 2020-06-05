package vdehorta.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsCategory;
import vdehorta.bean.dto.NewsCategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
@Service
public interface NewsCategoryMapper {

    NewsCategoryDto newsCategoryToNewsCategoryDto(NewsCategory newsCategory);

    List<NewsCategoryDto> newsCategoriesToNewsCategoryDtos(List<NewsCategory> newsCategories);
}
