package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.errors.WrongNewsFactIdException;
import vdehorta.service.mapper.NewsFactMapper;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class NewsFactServiceTest {

    private NewsFactService newsFactService;

    private NewsFactRepository newsFactRepositoryMock;

    private NewsFactMapper newsFactMapper = Mappers.getMapper(NewsFactMapper.class);

    @BeforeEach
    public void setup() {
        newsFactRepositoryMock = Mockito.mock(NewsFactRepository.class);
        newsFactService = new NewsFactService(newsFactRepositoryMock, newsFactMapper);
    }

    @Test
    void getAll_shouldReturnAllNewsFactsInNewsFactNoDetailDTOs() {

        String idNewsFact1 = "2";
        String idNewsFact2 = "po";

        String idNewsCategory1 = "id-newsCategory1";
        String idNewsCategory2 = "09099";

        LocationCoordinate locationCoordinate1 = new LocationCoordinate.Builder().x(1L).y(2L).build();
        LocationCoordinate locationCoordinate2 = new LocationCoordinate.Builder().x(222222222222L).y(-3333333333333L).build();

        //Given
        NewsFact newsFact1 = new NewsFact.Builder().id(idNewsFact1).newsCategoryId(idNewsCategory1).locationCoordinateX(locationCoordinate1.getX()).locationCoordinateY(locationCoordinate1.getY()).build();
        NewsFact newsFact2 = new NewsFact.Builder().id(idNewsFact2).newsCategoryId(idNewsCategory2).locationCoordinateX(locationCoordinate2.getX()).locationCoordinateY(locationCoordinate2.getY()).build();
        when(newsFactRepositoryMock.findAll()).thenReturn(Arrays.asList(newsFact1, newsFact2));

        //When
        List<NewsFactNoDetailDto> allNewsFacts = newsFactService.getAll();

        //Then
        assertThat(allNewsFacts)
            .hasSize(2)
            .extracting("id", "newsCategoryId", "locationCoordinate.x", "locationCoordinate.y")
            .containsOnly(
                //NewsFact1
                tuple(idNewsFact1, idNewsCategory1, locationCoordinate1.getX(), locationCoordinate1.getY()
                ),
                //NewsFact2
                tuple(idNewsFact2, idNewsCategory2, locationCoordinate2.getX(), locationCoordinate2.getY()));
    }

    @Test
    void getById_shouldReturnUniqueNewsFactMatchingGivenIdInNewsFactDetailDTO() {

        @NotNull String id1 = "id1";
        @NotNull String address1 = "address1";
        @NotNull String city1 = "city1";
        @NotNull String country1 = "country1";
        @NotNull Instant createdDate1 = LocalDate.parse("2020-04-02").atStartOfDay().toInstant(ZoneOffset.UTC);
        @NotNull Instant eventDate1 = LocalDate.parse("2020-04-01").atStartOfDay().toInstant(ZoneOffset.UTC);
        @NotNull LocationCoordinate locationCoordinate1 = new LocationCoordinate.Builder().x(1L).y(2L).build();
        @NotNull String newsCategoryId1 = "newsCategoryId";
        @NotNull String newsCategoryLabel1 = "newsCategoryLabel";
        @NotNull String videoPath1 = "videoPath1";

        @NotNull String id2 = "id2";
        @NotNull String address2 = "address2";
        @NotNull String city2 = "city2";
        @NotNull String country2 = "country2";
        @NotNull Instant createdDate2 = LocalDate.parse("2020-05-02").atStartOfDay().toInstant(ZoneOffset.UTC);
        @NotNull Instant eventDate2 = LocalDate.parse("2020-05-01").atStartOfDay().toInstant(ZoneOffset.UTC);
        @NotNull LocationCoordinate locationCoordinate2 = new LocationCoordinate.Builder().x(4L).y(1000L).build();
        @NotNull String newsCategoryId2 = "newsCategoryId2";
        @NotNull String newsCategoryLabel2 = "newsCategoryLabel2";
        @NotNull String videoPath2 = "videoPath2";

        //Given
        NewsFact newsFact1 = new NewsFact.Builder()
            .address(address1)
            .city(city1)
            .country(country1)
            .createdDate(createdDate1)
            .eventDate(eventDate1)
            .id(id1)
            .locationCoordinateX(locationCoordinate1.getX())
            .locationCoordinateY(locationCoordinate1.getY())
            .newsCategoryId(newsCategoryId1)
            .newsCategoryLabel(newsCategoryLabel1)
            .videoPath(videoPath1)
            .build();

        NewsFact newsFact2 = new NewsFact.Builder()
            .address(address2)
            .city(city2)
            .country(country2)
            .createdDate(createdDate2)
            .eventDate(eventDate2)
            .id(id2)
            .locationCoordinateX(locationCoordinate2.getX())
            .locationCoordinateY(locationCoordinate2.getY())
            .newsCategoryId(newsCategoryId2)
            .newsCategoryLabel(newsCategoryLabel2)
            .videoPath(videoPath2)
            .build();

        when(newsFactRepositoryMock.findById(id1)).thenReturn(Optional.of(newsFact1));
        when(newsFactRepositoryMock.findById(id2)).thenReturn(Optional.of(newsFact2));

        //When
        NewsFactDetailDto newsFactDetailDto = newsFactService.getById(id2);

        //Then
        assertThat(newsFactDetailDto.getAddress()).isEqualTo(address2);
        assertThat(newsFactDetailDto.getCity()).isEqualTo(city2);
        assertThat(newsFactDetailDto.getCountry()).isEqualTo(country2);
        assertThat(newsFactDetailDto.getCreatedDate()).isEqualTo(createdDate2);
        assertThat(newsFactDetailDto.getEventDate()).isEqualTo(eventDate2);
        assertThat(newsFactDetailDto.getId()).isEqualTo(id2);
        assertThat(newsFactDetailDto.getLocationCoordinate()).isEqualTo(locationCoordinate2);
        assertThat(newsFactDetailDto.getNewsCategoryId()).isEqualTo(newsCategoryId2);
        assertThat(newsFactDetailDto.getNewsCategoryLabel()).isEqualTo(newsCategoryLabel2);
        assertThat(newsFactDetailDto.getVideoPath()).isEqualTo(videoPath2);
    }

    @Test
    void getByOwner_shouldReturnOnlyNewsFactOwnedByUserWithGivenLogin() {

        @NotNull String ownerId = "idOwned";
        PageRequest pageable = PageRequest.of(1, 10);
        String ownerLogin = "Vivien";

        //Given
        NewsFact ownedNewsFact = new NewsFact.Builder().id(ownerId).owner(ownerLogin).build();
        when(newsFactRepositoryMock.findAllByOwner(pageable, ownerLogin)).thenReturn(new PageImpl<>(Collections.singletonList(ownedNewsFact)));

        //When
        Page<NewsFactDetailDto> byOwnerNewsFactPage = newsFactService.getByOwner(pageable, ownerLogin);

        //Then
        assertThat(byOwnerNewsFactPage.getContent()).extracting("id").containsOnly(ownerId);
    }

    @Test
    void getById_shouldThrowNewsFactNotFoundExceptionWhenGivenIdDoesNotExist() {

        //Given
        final String unexistingId = "unexisting_id";
        Mockito.when(newsFactRepositoryMock.findById(unexistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> newsFactService.getById(unexistingId))
            .isInstanceOf(WrongNewsFactIdException.class)
            .hasMessageContaining(unexistingId);
    }
}
