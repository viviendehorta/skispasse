package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import vdehorta.EntityTestUtil;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.UnexistingLoginException;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static vdehorta.EntityTestUtil.DEFAULT_DATE_FORMATTER;

class NewsFactServiceTest {

    private NewsFactService newsFactService;

    private NewsFactRepository newsFactRepositoryMock;

    private UserService userServiceMock;

    private NewsCategoryService newsCategoryServiceMock;

    private NewsFactMapper newsFactMapper = Mappers.getMapper(NewsFactMapper.class);

    private ClockService clockService = new ClockService();

    @BeforeEach
    public void setup() {
        newsFactRepositoryMock = Mockito.mock(NewsFactRepository.class);
        userServiceMock = Mockito.mock(UserService.class);
        newsCategoryServiceMock = Mockito.mock(NewsCategoryService.class);
        newsFactService = new NewsFactService(newsFactRepositoryMock, newsFactMapper, userServiceMock, newsCategoryServiceMock, clockService);
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
    void getByOwner_shouldReturnOnlyNewsFactOwnedByUserWithGivenLogin() {

        @NotNull String ownerId = "idOwned";
        PageRequest pageable = PageRequest.of(1, 10);
        String ownerLogin = "Vivien";

        //Given
        NewsFact ownedNewsFact = new NewsFact.Builder().id(ownerId).owner(ownerLogin).build();
        when(userServiceMock.getUserWithAuthoritiesByLogin(ownerLogin)).thenReturn(Optional.of(new User()));
        when(newsFactRepositoryMock.findAllByOwner(pageable, ownerLogin)).thenReturn(new PageImpl<>(Collections.singletonList(ownedNewsFact)));

        //When
        Page<NewsFactDetailDto> byOwnerNewsFactPage = newsFactService.getByOwner(pageable, ownerLogin);

        //Then
        assertThat(byOwnerNewsFactPage.getContent()).extracting("id").containsOnly(ownerId);
    }

    @Test
    void getByOwner_shouldThrowUnexistingLoginExceptionWhenNoUserWithGivenLoginExists() {

        String unexistingLogin = "unexisting-login";
        PageRequest pageable = PageRequest.of(1, 10);

        //Given
        when(userServiceMock.getUserWithAuthoritiesByLogin(unexistingLogin)).thenReturn(Optional.empty());

        //Assert-Thrown
        assertThatThrownBy(() -> newsFactService.getByOwner(pageable, unexistingLogin)).isInstanceOf(UnexistingLoginException.class);
    }
}
