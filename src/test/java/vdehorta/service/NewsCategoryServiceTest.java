package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import vdehorta.domain.NewsCategory;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.service.mapper.NewsCategoryMapper;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

class NewsCategoryServiceTest {

    private NewsCategoryService newsCategoryService;

    private NewsCategoryRepository newsCategoryRepositoryMock;

    private NewsCategoryMapper newsCategoryMapper = Mappers.getMapper(NewsCategoryMapper.class);

    @BeforeEach
    public void setup() {
        newsCategoryRepositoryMock = Mockito.mock(NewsCategoryRepository.class);
        newsCategoryService = new NewsCategoryService(newsCategoryRepositoryMock, newsCategoryMapper);
    }

    @Test
    void getAll_shouldReturnAllNewsCategories() {

        String newsCategoryId1 = "newsCategoryId1";
        String newsCategoryLabel1 = "newsCategoryLabel1";

        String newsCategoryId2 = "newsCategoryId2";
        String newsCategoryLabel2 = "newsCategoryLabel2";

        //Given
        NewsCategory newsCategory1 = new NewsCategory.Builder().id(newsCategoryId1).label(newsCategoryLabel1).build();
        NewsCategory newsCategory2 = new NewsCategory.Builder().id(newsCategoryId2).label(newsCategoryLabel2).build();
        when(newsCategoryRepositoryMock.findAll()).thenReturn(Arrays.asList(newsCategory1, newsCategory2));

        //When
        List<NewsCategoryDto> allNewsCategories = newsCategoryService.getAll();

        //Then
        assertThat(allNewsCategories)
            .hasSize(2)
            .extracting("id", "label")
            .containsOnly(
                // NewsCategory1
                tuple(newsCategoryId1, newsCategoryLabel1),
                // NewsCategory2
                tuple(newsCategoryId2, newsCategoryLabel2));
    }
}
