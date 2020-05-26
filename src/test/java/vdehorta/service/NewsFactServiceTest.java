package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.AuthenticationRequiredException;
import vdehorta.service.errors.MissingRoleException;
import vdehorta.service.errors.WrongNewsCategoryIdException;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
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
