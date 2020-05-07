package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import vdehorta.EntityTestUtil;
import vdehorta.domain.NewsCategory;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.service.errors.WrongNewsCategoryIdException;
import vdehorta.service.mapper.NewsCategoryMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @Test
    void getById_shouldReturnUniqueNewsCategoryDtoMatchingGivenId() {

        String id1 = "Id1";
        String id2 = "Id2";

        //Given
        NewsCategory newsCategory1 = EntityTestUtil.createDefaultNewsCategory1();
        newsCategory1.setId(id1);
        NewsCategory newsCategory2 = EntityTestUtil.createDefaultNewsCategory2();
        newsCategory2.setId(id2);

        when(newsCategoryRepositoryMock.findById(id1)).thenReturn(Optional.of(newsCategory1));
        when(newsCategoryRepositoryMock.findById(id2)).thenReturn(Optional.of(newsCategory2));

        //When
        NewsCategoryDto newsCategoryDto = newsCategoryService.getById(id2);

        //Then
        assertThat(newsCategoryDto.getId()).isEqualTo(id2);
        assertThat(newsCategoryDto.getLabel()).isEqualTo(newsCategory2.getLabel());
    }

    @Test
    void getById_shouldThrowWrongNewsFactIdExceptionWhenGivenIdDoesNotExist() {

        //Given
        final String unexistingId = "unexisting_id";
        Mockito.when(newsCategoryRepositoryMock.findById(unexistingId)).thenReturn(Optional.empty());

        //Assert-Thrown
        assertThatThrownBy(() -> newsCategoryService.getById(unexistingId))
            .isInstanceOf(WrongNewsCategoryIdException.class);
    }
}
