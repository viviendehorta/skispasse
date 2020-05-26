package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import vdehorta.EntityTestUtil;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.AuthenticationRequiredException;
import vdehorta.service.errors.MissingRoleException;
import vdehorta.service.errors.WrongNewsCategoryIdException;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static vdehorta.EntityTestUtil.DEFAULT_DATE_FORMATTER;
import static vdehorta.security.RoleEnum.*;

class NewsFactServiceTest extends AbstractMockedAuthenticationTest {

    private NewsFactService newsFactService;
    private NewsFactRepository newsFactRepositoryMock;
    private UserService userServiceMock;
    private NewsCategoryService newsCategoryServiceMock;
    private NewsFactMapper newsFactMapper = Mappers.getMapper(NewsFactMapper.class);
    private ClockService clockService = new ClockService();
    private VideoFileService videoFileServiceMock;

    @BeforeEach
    public void setup() {
        super.setup();
        newsFactRepositoryMock = Mockito.mock(NewsFactRepository.class);
        userServiceMock = Mockito.mock(UserService.class);
        newsCategoryServiceMock = Mockito.mock(NewsCategoryService.class);
        videoFileServiceMock = Mockito.mock(VideoFileService.class);
        newsFactService = new NewsFactService(
                newsFactRepositoryMock,
                newsFactMapper,
                userServiceMock,
                newsCategoryServiceMock,
                clockService,
                videoFileServiceMock,
                authenticationServiceMock);
    }

    @Test
    void getAll_shouldReturnAllNewsFactsInNewsFactNoDetailDTOs() {

        //Given
        NewsFact newsFact1 = EntityTestUtil.createDefaultNewsFact1();
        NewsFact newsFact2 = EntityTestUtil.createDefaultNewsFact2();
        when(newsFactRepositoryMock.findAll()).thenReturn(Arrays.asList(newsFact1, newsFact2));

        //When
        List<NewsFactNoDetailDto> allNewsFacts = newsFactService.getAll();

        //Then
        assertThat(allNewsFacts)
                .hasSize(2)
                .extracting("id", "newsCategoryId", "locationCoordinate.x", "locationCoordinate.y")
                .containsOnly(
                        //NewsFact1
                        tuple(newsFact1.getId(), newsFact1.getNewsCategoryId(), newsFact1.getLocationCoordinateX(), newsFact1.getLocationCoordinateY()
                        ),
                        //NewsFact2
                        tuple(newsFact2.getId(), newsFact2.getNewsCategoryId(), newsFact2.getLocationCoordinateX(), newsFact2.getLocationCoordinateY()));
    }

    @Test
    void getById_shouldReturnUniqueNewsFactDetailDtoMatchingGivenId() {

        String id1 = "Id1";
        String id2 = "Id2";

        //Given
        NewsFact newsFact1 = EntityTestUtil.createDefaultNewsFact1();
        newsFact1.setId(id1);
        NewsFact newsFact2 = EntityTestUtil.createDefaultNewsFact2();
        newsFact2.setId(id2);

        when(newsFactRepositoryMock.findById(id1)).thenReturn(Optional.of(newsFact1));
        when(newsFactRepositoryMock.findById(id2)).thenReturn(Optional.of(newsFact2));

        //When
        NewsFactDetailDto newsFactDetailDto = newsFactService.getById(id2);

        //Then
        assertThat(newsFactDetailDto.getAddress()).isEqualTo(newsFact2.getAddress());
        assertThat(newsFactDetailDto.getCity()).isEqualTo(newsFact2.getCity());
        assertThat(newsFactDetailDto.getCountry()).isEqualTo(newsFact2.getCountry());
        assertThat(newsFactDetailDto.getCreatedDate()).isEqualTo(DEFAULT_DATE_FORMATTER.format(newsFact2.getCreatedDate()));
        assertThat(newsFactDetailDto.getEventDate()).isEqualTo(DEFAULT_DATE_FORMATTER.format(newsFact2.getEventDate()));
        assertThat(newsFactDetailDto.getId()).isEqualTo(id2);
        assertThat(newsFactDetailDto.getLocationCoordinate()).isNotNull();
        assertThat(newsFactDetailDto.getLocationCoordinate().getX()).isEqualTo(newsFact2.getLocationCoordinateX());
        assertThat(newsFactDetailDto.getLocationCoordinate().getY()).isEqualTo(newsFact2.getLocationCoordinateY());
        assertThat(newsFactDetailDto.getNewsCategoryId()).isEqualTo(newsFact2.getNewsCategoryId());
        assertThat(newsFactDetailDto.getNewsCategoryLabel()).isEqualTo(newsFact2.getNewsCategoryLabel());
        assertThat(newsFactDetailDto.getVideoPath()).isEqualTo(newsFact2.getVideoPath());
    }

    @Test
    void getById_shouldThrowWrongNewsFactIdExceptionWhenGivenIdDoesNotExist() {

        //Given
        final String unexistingId = "unexisting_id";
        Mockito.when(newsFactRepositoryMock.findById(unexistingId)).thenReturn(Optional.empty());

        //Assert-Thrown
        assertThatThrownBy(() -> newsFactService.getById(unexistingId))
                .isInstanceOf(WrongNewsFactIdException.class);
    }

    @Test
    void getMyNewsFacts_shouldThrowErrorIfUserIsNotLoggedIn() {
        super.mockAnonymousUser();
        assertThatThrownBy(() -> newsFactService.getMyNewsFacts(null)).isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    void getMyNewsFacts_shouldThrowForbiddenActionExceptionForNoneContributorUsers() {

        //USER role
        super.mockAuthenticated(USER);
        assertThatThrownBy(() -> newsFactService.getMyNewsFacts(null)).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //ADMIN role
        super.mockAuthenticated(ADMIN);
        assertThatThrownBy(() -> newsFactService.getMyNewsFacts(null)).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //Various roles
        super.mockAuthenticated(Arrays.asList(USER, ADMIN));
        assertThatThrownBy(() -> newsFactService.getMyNewsFacts(null)).isInstanceOf(MissingRoleException.class);

    }

    @Test
    void update_shouldThrowAuthenticationRequiredExceptionIfUserIsNotLoggedIn() {
        super.mockAnonymousUser();
        assertThatThrownBy(() -> newsFactService.update(new NewsFactDetailDto())).isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    void update_shouldThrowForbiddenActionExceptionForNoneContributorUsers() {

        //USER role
        super.mockAuthenticated(USER);
        assertThatThrownBy(() -> newsFactService.update(new NewsFactDetailDto())).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //ADMIN role
        super.mockAuthenticated(ADMIN);
        assertThatThrownBy(() -> newsFactService.update(new NewsFactDetailDto())).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //Various roles
        super.mockAuthenticated(Arrays.asList(USER, ADMIN));
        assertThatThrownBy(() -> newsFactService.update(new NewsFactDetailDto())).isInstanceOf(MissingRoleException.class);
    }

    @Test
    void update_shouldThrowExceptionIfNewsFactIdIsNull() {
        NewsFactDetailDto nullIdNewsFact = new NewsFactDetailDto.Builder().id(null).build();
        assertThatThrownBy(() -> newsFactService.update(nullIdNewsFact)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void update_shouldThrowExceptionIfEventDateFormatIsWrong() {

        //Given
        NewsFactDetailDto.Builder builder = new NewsFactDetailDto.Builder().id("id");


        //Assert-Thrown

        //format dd-MM-yyyy
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("17-04-1989").build())).isInstanceOf(DateTimeParseException.class);

        //format dd/MM/yyyy
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("17/04/1989").build())).isInstanceOf(DateTimeParseException.class);

        //format yyyy/MM/dd
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("1989/04/17").build())).isInstanceOf(DateTimeParseException.class);

        //format yyyy/M/dd
        assertThatThrownBy(() -> newsFactService.update(builder.eventDate("1989/4/17").build())).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void update_shouldThrowExceptionWhenNewsCategoryIdDoesntExist() {
        String unexistingCategoryId = "unexisting";

        //Given
        when(newsCategoryServiceMock.getById(unexistingCategoryId)).thenThrow(new WrongNewsCategoryIdException(unexistingCategoryId));
        NewsFactDetailDto toUpdateNewsFact = new NewsFactDetailDto.Builder()
                .newsCategoryId(unexistingCategoryId)
                .id("id")
                .eventDate("1989-04-17")
                .build();

        //Assert-Thrown
        assertThatThrownBy(() -> newsFactService.update(toUpdateNewsFact)).isInstanceOf(WrongNewsCategoryIdException.class);
    }

    @Test
    void create_shouldThrowErrorIfUserIsNotLoggedIn() {
        super.mockAnonymousUser();
        assertThatThrownBy(() -> newsFactService.create(null, null)).isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    void create_shouldThrowForbiddenActionExceptionForANoneContributorUsers() {

        //USER
        super.mockAuthenticated(USER);
        assertThatThrownBy(() -> newsFactService.create(null, null)).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //ADMIN
        super.mockAuthenticated(Arrays.asList(ADMIN, USER));
        assertThatThrownBy(() -> newsFactService.create(null, null)).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //MULTI-ROLES
        super.mockAuthenticated(Arrays.asList(ADMIN, USER));
        assertThatThrownBy(() -> newsFactService.create(null, null)).isInstanceOf(MissingRoleException.class);
    }

    @Test
    void create_shouldThrowExceptionIfNewsFactIdIsNotNull() {
        super.mockAuthenticated(CONTRIBUTOR);
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder().id("idExistsAtCreationWtf!").build();
        assertThatThrownBy(() -> newsFactService.create(newsFact, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void delete_shouldThrowErrorIfUserIsNotLoggedIn() {
        super.mockAnonymousUser();
        assertThatThrownBy(() -> newsFactService.delete(null)).isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    void delete_shouldThrowForbiddenActionExceptionForANoneContributorUsers() {

        //USER
        super.mockAuthenticated(USER);
        assertThatThrownBy(() -> newsFactService.delete(null)).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //ADMIN
        super.mockAuthenticated(Arrays.asList(ADMIN, USER));
        assertThatThrownBy(() -> newsFactService.delete(null)).isInstanceOf(MissingRoleException.class);

        Mockito.reset(authenticationServiceMock);

        //MULTI-ROLES
        super.mockAuthenticated(Arrays.asList(ADMIN, USER));
        assertThatThrownBy(() -> newsFactService.delete(null)).isInstanceOf(MissingRoleException.class);
    }

    @Test
    void delete_shouldThrowWrongNewsFactIdExceptionWhenIdDoesntExist() {

        //Given
        super.mockAuthenticated(CONTRIBUTOR);
        when(newsFactRepositoryMock.findById("unexistingId")).thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> newsFactService.delete("unexistingId")).isInstanceOf(WrongNewsFactIdException.class);
    }

}
