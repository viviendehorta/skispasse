package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import vdehorta.utils.BeanTestUtils;
import vdehorta.domain.NewsFact;
import vdehorta.bean.dto.NewsCategoryDto;
import vdehorta.bean.dto.NewsFactDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.NewsFactAccessForbiddenException;
import vdehorta.service.errors.NewsFactNotFoundException;
import vdehorta.service.errors.UnexistingNewsCategoryException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class NewsFactServiceTest {

    private NewsFactService newsFactService;
    private NewsFactRepository newsFactRepositoryMock;
    private NewsCategoryService newsCategoryServiceMock;
    private NewsFactMapper newsFactMapper;
    private ClockService clockService = new ClockService();
    private VideoService videoFileServiceMock;


    @BeforeEach
    public void setup() {
        newsFactMapper = Mappers.getMapper(NewsFactMapper.class);
        newsFactRepositoryMock = Mockito.mock(NewsFactRepository.class);
        newsCategoryServiceMock = Mockito.mock(NewsCategoryService.class);
        videoFileServiceMock = Mockito.mock(VideoService.class);
        newsFactService = new NewsFactService(
                newsFactRepositoryMock,
                newsFactMapper,
                newsCategoryServiceMock,
                clockService,
                videoFileServiceMock);
    }

    @Test
    void getById_shouldThrowErrorIfGivenIdDoesNotExist() {

        //Given
        final String unexistingId = "unexisting_id";
        Mockito.when(newsFactRepositoryMock.findById(unexistingId)).thenReturn(Optional.empty());

        //Assert-Thrown
        assertThatThrownBy(() -> newsFactService.getById(unexistingId))
                .isInstanceOf(NewsFactNotFoundException.class);
    }

    @Test
    void update_shouldThrowErrorIfNewsFactIdIsNull() {
        NewsFactDetailDto nullIdNewsFact = new NewsFactDetailDto.Builder().id(null).build();
        assertThatThrownBy(() -> newsFactService.update(nullIdNewsFact, "Mandela")).isInstanceOf(NullPointerException.class);
    }

    @Test
    void update_shouldThrowErrorIfEventDateFormatIsWrong() {

        //Given
        NewsFactDetailDto.Builder builder = new NewsFactDetailDto.Builder().id("id");

        //format dd-MM-yyyy
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("17-04-1989").build(), null)).isInstanceOf(DateTimeParseException.class);

        //format dd/MM/yyyy
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("17/04/1989").build(), null)).isInstanceOf(DateTimeParseException.class);

        //format yyyy/MM/dd
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("1989/04/17").build(), null)).isInstanceOf(DateTimeParseException.class);

        //format yyyy/M/dd
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("1989/4/17").build(), null)).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void update_shouldThrowErrorIfNewsCategoryIdDoesntExist() {
        String unexistingCategoryId = "unexisting";

        //Given
        when(newsCategoryServiceMock.getById(unexistingCategoryId)).thenThrow(new UnexistingNewsCategoryException(unexistingCategoryId));
        NewsFactDetailDto toUpdateNewsFact = new NewsFactDetailDto.Builder()
                .newsCategoryId(unexistingCategoryId)
                .id("id")
                .eventDate("1989-04-17")
                .build();

        //Assert-Thrown
        assertThatThrownBy(() -> newsFactService.update(toUpdateNewsFact, null)).isInstanceOf(UnexistingNewsCategoryException.class);
    }

    @Test
    void update_shouldThrowErrorIfGivenLoginDoesntMatchOwner() {

        String newsFactId = "newsFactId";
        String categoryId = "categoryId";

        //Given
        NewsFactDetailDto toUpdateNewsFactDto = new NewsFactDetailDto.Builder()
                .eventDate("1989-04-17")
                .id(newsFactId)
                .newsCategoryId(categoryId)
                .build();
        NewsFact existingNewsFact = BeanTestUtils.createDefaultNewsFact();
        existingNewsFact.setOwner("Mandela");
        when(newsCategoryServiceMock.getById(categoryId)).thenReturn(new NewsCategoryDto.Builder().id(categoryId).build());
        when(newsFactRepositoryMock.findById(newsFactId)).thenReturn(Optional.of(existingNewsFact));

        //Assert-Thrown
        assertThatThrownBy(() -> newsFactService.update(toUpdateNewsFactDto, "Picasso")).isInstanceOf(NewsFactAccessForbiddenException.class);
    }

    @Test
    void create_shouldThrowErrorIfNewsFactIdIsNotNull() {
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder().id("idExistsAtCreationWtf!").build();
        assertThatThrownBy(() -> newsFactService.create(newsFact, null, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void delete_shouldThrowErrorIfNewsFactIdDoesntExist() {
        when(newsFactRepositoryMock.findById("unexistingId")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> newsFactService.delete("unexistingId", null)).isInstanceOf(NewsFactNotFoundException.class);
    }

    @Test
    void delete_shouldThrowErrorIfDeleterIsNotOwner() {
        String newsFactId = "id";
        NewsFact toDelete = BeanTestUtils.createDefaultNewsFact();
        toDelete.setOwner("Van Gogh");

        when(newsFactRepositoryMock.findById(newsFactId)).thenReturn(Optional.of(toDelete));
        assertThatThrownBy(() -> newsFactService.delete(newsFactId, "Deleter Picasso")).isInstanceOf(NewsFactNotFoundException.class);
    }

}
