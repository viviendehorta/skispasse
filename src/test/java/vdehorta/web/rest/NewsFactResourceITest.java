package vdehorta.web.rest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.Problem;
import vdehorta.bean.dto.NewsFactDetailDto;
import vdehorta.bean.dto.NewsFactNoDetailDto;
import vdehorta.config.ApplicationProperties;
import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.repository.NewsFactRepository;
import vdehorta.repository.UserRepository;
import vdehorta.service.AuthenticationService;
import vdehorta.service.ClockService;
import vdehorta.service.MediaService;
import vdehorta.service.util.DateUtil;
import vdehorta.utils.BeanTestUtils;
import vdehorta.utils.PersistenceTestUtils;
import vdehorta.utils.RestTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.fill;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static vdehorta.bean.ContentTypeEnum.*;
import static vdehorta.bean.MediaType.PHOTO;
import static vdehorta.bean.MediaType.VIDEO;
import static vdehorta.security.RoleEnum.*;
import static vdehorta.utils.BeanTestUtils.*;
import static vdehorta.utils.JsonTestUtils.toJsonString;
import static vdehorta.utils.RestTestUtils.*;

/**
 * Integration tests for the {@link NewsFactResource} REST controller.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsFactResourceITest {

    @Autowired
    private ClockService clockService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private NewsFactRepository newsFactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private GridFsTemplate mediaGridFsTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @BeforeEach
    public void setup() {
        PersistenceTestUtils.resetDatabase(mongoTemplate, applicationProperties);
        mockAnonymous(authenticationService); //By default, mock anonymous authentication
    }

    @Test
    public void getAll_caseOk() {

        NewsFact newsFact1 = createDefaultNewsFact1();
        NewsFact newsFact2 = createDefaultNewsFact2();
        newsFactRepository.saveAll(asList(newsFact1, newsFact2));

        ResponseEntity<NewsFactNoDetailDto[]> response = testRestTemplate.getForEntity("/newsFacts/all", NewsFactNoDetailDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .hasSize(2)
                .extracting("id", "newsCategoryId", "locationCoordinate.x", "locationCoordinate.y")
                .containsOnly(
                        tuple("news_fact_id1", "newsCategoryId1", DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y),
                        tuple("news_fact_id2", "newsCategoryId2", DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y));
    }

    @Test
    public void getById_caseOk() {

        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setMediaId(DEFAULT_MEDIA_ID);
        newsFact.setMediaType(DEFAULT_MEDIA_TYPE.name());
        newsFactRepository.save(newsFact);

        ResponseEntity<NewsFactDetailDto> response = testRestTemplate.getForEntity("/newsFacts/" + newsFact.getId(), NewsFactDetailDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        NewsFactDetailDto resultNewsFact = response.getBody();
        assertThat(resultNewsFact.getAddressDetail()).isEqualTo(DEFAULT_ADDRESS_DETAIL);
        assertThat(resultNewsFact.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(resultNewsFact.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(resultNewsFact.getCreatedDate()).isEqualTo(DEFAULT_DATE_FORMATTER.format(DEFAULT_CREATED_DATE));
        assertThat(resultNewsFact.getEventDate()).isEqualTo(DEFAULT_DATE_FORMATTER.format(DEFAULT_EVENT_DATE));
        assertThat(resultNewsFact.getNewsCategoryId()).isEqualTo(DEFAULT_NEWS_CATEGORY_ID);
        assertThat(resultNewsFact.getNewsCategoryLabel()).isEqualTo(DEFAULT_NEWS_CATEGORY_LABEL);
        assertThat(resultNewsFact.getMedia().getType()).isEqualTo(DEFAULT_MEDIA_TYPE);
    }

    @Test
    public void getById_shouldThrowNotFoundIfGivenIdDoesNotExist() {

        userRepository.save(createDefaultUser());

        //User is useless for this test but better to have a persisted user
        NewsFact newsFact = createDefaultNewsFact1();
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);

        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/unexistingId", Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo("News fact with id 'unexistingId' was not found!");
    }

    @Test
    public void getMyNewsFacts_caseOk() {

        String login = "polo";
        mockAuthentication(authenticationService, login, CONTRIBUTOR);

        User poloUser = createDefaultUser1();
        poloUser.setLogin(login);
        userRepository.save(poloUser);

        NewsFact newsFact1 = createDefaultNewsFact1();
        newsFact1.setOwner("marco");
        NewsFact newsFact2 = createDefaultNewsFact2();
        newsFact2.setOwner(login);
        newsFactRepository.saveAll(asList(newsFact1, newsFact2));

        //Then
        ResponseEntity<NewsFactDetailDto[]> response = testRestTemplate.getForEntity("/newsFacts/contributor", NewsFactDetailDto[].class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .hasSize(1)
                .extracting("id", "newsCategoryId").containsOnly(tuple("news_fact_id2", "newsCategoryId2"));
    }

    @Test
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsNotAuthenticated() {
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/contributor", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
        assertThat(response.getBody().getDetail()).isEqualTo("Authentication is required!");
    }

    @Test
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsOnlyAdmin() {
        mockAuthentication(authenticationService, "skisp", ADMIN);
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/contributor", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody().getDetail()).isEqualTo("Role 'contributor' is required!");
    }

    @Test
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsOnlyUser() {
        mockAuthentication(authenticationService, "skisp", USER);
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/contributor", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody().getDetail()).isEqualTo("Role 'contributor' is required!");
    }

    @Test
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsAdminAndUser() {
        mockAuthentication(authenticationService, "skisp", asList(ADMIN, USER));
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/contributor", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody().getDetail()).isEqualTo("Role 'contributor' is required!");
    }

    @Test
    void createVideoNewsFact_caseOk() throws IOException {
        mockAuthentication(authenticationService, "zeus", asList(USER, CONTRIBUTOR));

        // Init ClockService with a fix clock to be able to assert the date values
        LocalDateTime fixedNow = LocalDateTime.parse("2020-03-24T20:30:23");
        clockService.setClock(Clock.fixed(fixedNow.toInstant(ZoneOffset.UTC), ZoneId.of("Z"))); // "Z" for UTC time zone

        // Initialize database
        NewsCategory newsCategory = createDefaultNewsCategory1();
        newsCategoryRepository.save(newsCategory);
        final int newsFactInitialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto toCreateNewsFact = createDefaultNewsFactDetailDto();
        toCreateNewsFact.setId(null);
        toCreateNewsFact.setNewsCategoryId(newsCategory.getId());
        toCreateNewsFact.setNewsCategoryLabel(null);

        ResponseEntity<NewsFactDetailDto> response = testRestTemplate.postForEntity(
                "/newsFacts",
                createFileAndJsonMultipartEntity(
                        "mediaFile", "skispasse.ogv", "video file content".getBytes(), OGG.getContentType(), "newsFactJson", toJsonString(toCreateNewsFact)),
                NewsFactDetailDto.class);

        //Check HTTP response
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        NewsFactDetailDto resultNewsFact = response.getBody();
        assertThat(resultNewsFact).isNotNull();
        assertThat(resultNewsFact.getNewsCategoryId()).isEqualTo(newsCategory.getId());
        assertThat(resultNewsFact.getNewsCategoryLabel()).isEqualTo(newsCategory.getLabel());
        assertThat(resultNewsFact.getEventDate()).isEqualTo(DEFAULT_DATE_FORMATTER.format(DEFAULT_EVENT_DATE));
        assertThat(resultNewsFact.getCreatedDate()).isEqualTo("2020-03-24");
        assertThat(resultNewsFact.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(resultNewsFact.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(resultNewsFact.getAddressDetail()).isEqualTo(DEFAULT_ADDRESS_DETAIL);
        assertThat(resultNewsFact.getId()).isNotEmpty();
        assertThat(resultNewsFact.getLocationCoordinate().getX()).isEqualTo(DEFAULT_LOCATION_COORDINATE_X);
        assertThat(resultNewsFact.getLocationCoordinate().getY()).isEqualTo(DEFAULT_LOCATION_COORDINATE_Y);
        assertThat(resultNewsFact.getMedia()).isNotNull();
        assertThat(resultNewsFact.getMedia().getType()).isEqualTo(VIDEO);
        assertThat(resultNewsFact.getMedia().getContentType()).isEqualTo(OGG);

        //Check news fact persistence
        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount + 1);
        NewsFact persistedNewsFact = newsFactRepository.findById(resultNewsFact.getId()).orElse(null);
        assertThat(persistedNewsFact).isNotNull();
        assertThat(persistedNewsFact.getAddressDetail()).isEqualTo(DEFAULT_ADDRESS_DETAIL);
        assertThat(persistedNewsFact.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(persistedNewsFact.getCreatedBy()).isEqualTo("zeus");
        assertThat(persistedNewsFact.getCreatedDate()).isEqualToIgnoringSeconds(fixedNow);
        assertThat(persistedNewsFact.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(persistedNewsFact.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(persistedNewsFact.getLastModifiedBy()).isEqualTo("zeus");
        assertThat(persistedNewsFact.getLastModifiedDate()).isEqualToIgnoringSeconds(fixedNow);
        assertThat(persistedNewsFact.getLocationCoordinateX()).isEqualTo(DEFAULT_LOCATION_COORDINATE_X);
        assertThat(persistedNewsFact.getLocationCoordinateY()).isEqualTo(DEFAULT_LOCATION_COORDINATE_Y);
        assertThat(persistedNewsFact.getNewsCategoryId()).isEqualTo(newsCategory.getId());
        assertThat(persistedNewsFact.getNewsCategoryLabel()).isEqualTo(newsCategory.getLabel());
        assertThat(persistedNewsFact.getOwner()).isEqualTo("zeus");
        assertThat(persistedNewsFact.getMediaId()).isNotEmpty();
        assertThat(persistedNewsFact.getMediaType()).isEqualTo(VIDEO.name());
        assertThat(persistedNewsFact.getMediaContentType()).isEqualTo(OGG.getContentType());

        //Check video file persistence
        MongoCursor<GridFSFile> persistedVideoCursor = mediaGridFsTemplate.find(new Query().addCriteria(Criteria.where("_id").is(persistedNewsFact.getMediaId()))).iterator();
        assertThat(persistedVideoCursor.hasNext()).isTrue();

        GridFSFile persistedVideoFile = persistedVideoCursor.next();
        assertThat(persistedVideoCursor.hasNext()).isFalse(); //Check there is only 1 matching file

        assertThat(persistedVideoFile.getFilename()).isEqualTo("zeus_2020-03-24_20:30:23_OGG");
        assertThat(persistedVideoFile.getMetadata().getString("owner")).isEqualTo("zeus");
        assertThat(persistedVideoFile.getMetadata().getString("_contentType")).isEqualTo(OGG.getContentType());
    }

    @Test
    void createPhotoNewsFact_caseOk() throws IOException {
        mockAuthentication(authenticationService, "cedric", CONTRIBUTOR);

        // Init ClockService with a fix clock to be able to assert the date values
        LocalDateTime fixedNow = LocalDateTime.parse("2020-09-03T20:24:00");
        clockService.setClock(Clock.fixed(fixedNow.toInstant(ZoneOffset.UTC), ZoneId.of("Z"))); // "Z" for UTC time zone

        // Initialize database
        NewsCategory newsCategory = createDefaultNewsCategory1();
        newsCategoryRepository.save(newsCategory);
        final int newsFactInitialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto toCreateNewsFact = createDefaultNewsFactDetailDto();
        toCreateNewsFact.setId(null);
        toCreateNewsFact.setNewsCategoryId(newsCategory.getId());
        toCreateNewsFact.setNewsCategoryLabel(null);

        ResponseEntity<NewsFactDetailDto> response = testRestTemplate.postForEntity(
                "/newsFacts",
                createFileAndJsonMultipartEntity(
                        "mediaFile", "skispasse.jpg", "image content".getBytes(), JPEG.getContentType(), "newsFactJson", toJsonString(toCreateNewsFact)),
                NewsFactDetailDto.class);

        //Check HTTP response
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        NewsFactDetailDto resultNewsFact = response.getBody();
        assertThat(resultNewsFact).isNotNull();
        assertThat(resultNewsFact.getNewsCategoryId()).isEqualTo(newsCategory.getId());
        assertThat(resultNewsFact.getNewsCategoryLabel()).isEqualTo(newsCategory.getLabel());
        assertThat(resultNewsFact.getEventDate()).isEqualTo(DEFAULT_DATE_FORMATTER.format(DEFAULT_EVENT_DATE));
        assertThat(resultNewsFact.getCreatedDate()).isEqualTo("2020-09-03");
        assertThat(resultNewsFact.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(resultNewsFact.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(resultNewsFact.getAddressDetail()).isEqualTo(DEFAULT_ADDRESS_DETAIL);
        assertThat(resultNewsFact.getId()).isNotEmpty();
        assertThat(resultNewsFact.getLocationCoordinate().getX()).isEqualTo(DEFAULT_LOCATION_COORDINATE_X);
        assertThat(resultNewsFact.getLocationCoordinate().getY()).isEqualTo(DEFAULT_LOCATION_COORDINATE_Y);
        assertThat(resultNewsFact.getMedia()).isNotNull();
        assertThat(resultNewsFact.getMedia().getType()).isEqualTo(PHOTO);
        assertThat(resultNewsFact.getMedia().getContentType()).isEqualTo(JPEG);

        //Check news fact persistence
        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount + 1);
        NewsFact persistedNewsFact = newsFactRepository.findById(resultNewsFact.getId()).orElse(null);
        assertThat(persistedNewsFact).isNotNull();
        assertThat(persistedNewsFact.getAddressDetail()).isEqualTo(DEFAULT_ADDRESS_DETAIL);
        assertThat(persistedNewsFact.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(persistedNewsFact.getCreatedBy()).isEqualTo("cedric");
        assertThat(persistedNewsFact.getCreatedDate()).isEqualToIgnoringSeconds(fixedNow);
        assertThat(persistedNewsFact.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(persistedNewsFact.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(persistedNewsFact.getLastModifiedBy()).isEqualTo("cedric");
        assertThat(persistedNewsFact.getLastModifiedDate()).isEqualToIgnoringSeconds(fixedNow);
        assertThat(persistedNewsFact.getLocationCoordinateX()).isEqualTo(DEFAULT_LOCATION_COORDINATE_X);
        assertThat(persistedNewsFact.getLocationCoordinateY()).isEqualTo(DEFAULT_LOCATION_COORDINATE_Y);
        assertThat(persistedNewsFact.getNewsCategoryId()).isEqualTo(newsCategory.getId());
        assertThat(persistedNewsFact.getNewsCategoryLabel()).isEqualTo(newsCategory.getLabel());
        assertThat(persistedNewsFact.getOwner()).isEqualTo("cedric");
        assertThat(persistedNewsFact.getMediaId()).isNotEmpty();
        assertThat(persistedNewsFact.getMediaType()).isEqualTo(PHOTO.name());
        assertThat(persistedNewsFact.getMediaContentType()).isEqualTo(JPEG.getContentType());

        //Check video file persistence
        MongoCursor<GridFSFile> persistedMediaCursor = mediaGridFsTemplate.find(new Query().addCriteria(Criteria.where("_id").is(persistedNewsFact.getMediaId()))).iterator();
        assertThat(persistedMediaCursor.hasNext()).isTrue();

        GridFSFile persistedMediaFile = persistedMediaCursor.next();
        assertThat(persistedMediaCursor.hasNext()).isFalse(); //Check there is only 1 matching file

        assertThat(persistedMediaFile.getFilename()).isEqualTo("cedric_2020-09-03_20:24:00_JPEG");
        assertThat(persistedMediaFile.getMetadata().getString("owner")).isEqualTo("cedric");
        assertThat(persistedMediaFile.getMetadata().getString("_contentType")).isEqualTo(JPEG.getContentType());
    }

    @Test
    void createNewsFact_shouldThrowExceptionForNoneContributorUser() throws IOException {
        mockAuthentication(authenticationService, "toto", asList(USER, ADMIN));

        // Initialize database
        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto newsFactDto = new NewsFactDetailDto.Builder().build();

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/newsFacts",
                createFileAndJsonMultipartEntity(
                        "mediaFile", "skispasse.mp4", "video file content".getBytes(), MP4.getContentType(), "newsFactJson", toJsonString(newsFactDto)), Problem.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody().getDetail()).isEqualTo("Role 'contributor' is required!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void createNewsFact_shouldThrowExceptionForAnonymousUser() throws IOException {
        mockAnonymous(authenticationService);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFactDto = new NewsFactDetailDto.Builder().build();

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/newsFacts",
                createFileAndJsonMultipartEntity(
                        "mediaFile", "skispasse.mp4", "video file content".getBytes(), MP4.getContentType(), "newsFactJson", toJsonString(newsFactDto)), Problem.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
        assertThat(response.getBody().getDetail()).isEqualTo("Authentication is required!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void createNewsFact_shouldThrowErrorIfVideoFileIsTooBig() throws IOException {
        mockAuthentication(authenticationService, "pequi", CONTRIBUTOR);

        // Initialize database
        final int newsFactInitialCount = newsFactRepository.findAll().size();
        final int videoInitialCount = countGridFsVideos();

        //Given

        //News fact
        NewsFactDetailDto newsFact = BeanTestUtils.createDefaultNewsFactDetailDto();
        newsFact.setId(null);

        //File content preparation
        byte[] fileBytes = new byte[50 * 1024]; //100KB file content
        fill(fileBytes, (byte) 1);

        //When
        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/newsFacts",
                createFileAndJsonMultipartEntity(
                        "mediaFile", "tooBigVideo.mp4", fileBytes, MP4.getContentType(), "newsFactJson", toJsonString(newsFact)), Problem.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).contains("Maximum upload size exceeded");
        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount);
        assertThat(countGridFsVideos()).isEqualTo(videoInitialCount);
    }

    @Test
    void createNewsFact_shouldNotPersistNewsFactIfErrorOccursPersistingVideo() throws IOException {
        RestTestUtils.mockAuthentication(authenticationService, "ares", asList(USER, CONTRIBUTOR));

        // Initialize database
        NewsCategory newsCategory = createDefaultNewsCategory1();
        newsCategoryRepository.save(newsCategory);
        final int newsFactInitialCount = newsFactRepository.findAll().size();
        final int videoInitialCount = countGridFsVideos();

        //Given
        NewsFactDetailDto toCreateNewsFact = createDefaultNewsFactDetailDto();
        toCreateNewsFact.setId(null);
        toCreateNewsFact.setNewsCategoryId(newsCategory.getId());
        toCreateNewsFact.setNewsCategoryLabel(null);

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/newsFacts",
                createFileAndJsonMultipartEntity(
                        "mediaFile", "skispasse.txt", "text content".getBytes(), "text/plain", "newsFactJson", toJsonString(toCreateNewsFact)), Problem.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody().getDetail()).isEqualTo("Unsuppported media type 'text/plain'!");
        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount);
        assertThat(countGridFsVideos()).isEqualTo(videoInitialCount);
    }

    @Test
    public void deleteNewsFact_shouldDeleteTheNewsFactAndHisVideo() {
        mockAuthentication(authenticationService, "titi", CONTRIBUTOR);

        String owner = "titi";

        //Init NewsFact video
        String videoId = this.mediaGridFsTemplate.store(
                new ByteArrayInputStream("video-content".getBytes()),
                "ma-video",
                new Document().append(MediaService.OWNER_METADATA_KEY, owner)).toString();
        //Init NewsFact
        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setOwner(owner);
        newsFact.setMediaId(videoId);
        newsFactRepository.save(newsFact);

        int initialNewsFactCount = newsFactRepository.findAll().size();
        int initialVideoFilesCount = countGridFsVideos();

        // Then
        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts/" + DEFAULT_NEWS_FACT_ID, DELETE, HttpEntity.EMPTY, Problem.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(newsFactRepository.findAll()).hasSize(initialNewsFactCount - 1);
        assertThat(countGridFsVideos()).isEqualTo(initialVideoFilesCount - 1);
        assertThat(newsFactRepository.findById(newsFact.getId())).isEmpty();
        assertThat(mediaGridFsTemplate.findOne(new Query(Criteria.where("_id").is(videoId)))).isNull();
    }

    @Test
    public void deleteNewsFact_shouldNotDeleteVideoIfNewsFactDeletionFailed() {
        mockAuthentication(authenticationService, "bandido", CONTRIBUTOR);

        //Init NewsFact video
        String videoId = this.mediaGridFsTemplate.store(
                new ByteArrayInputStream("video-content".getBytes()),
                "ma-video",
                new Document().append(MediaService.OWNER_METADATA_KEY, "owner")).toString();
        //Init NewsFact
        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setOwner("owner");
        newsFact.setMediaId(videoId);
        newsFactRepository.save(newsFact);

        int initialNewsFactCount = newsFactRepository.findAll().size();
        int initialVideoFilesCount = countGridFsVideos();

        // Then
        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts/" + DEFAULT_NEWS_FACT_ID, DELETE, HttpEntity.EMPTY, Problem.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(newsFactRepository.findAll()).hasSize(initialNewsFactCount);
        assertThat(countGridFsVideos()).isEqualTo(initialVideoFilesCount);
        assertThat(newsFactRepository.findById(newsFact.getId())).isPresent();
        assertThat(mediaGridFsTemplate.findOne(new Query(Criteria.where("_id").is(videoId)))).isNotNull();
    }

    @Test
    public void deleteNewsFact_shouldThrowNotFoundIfGivenIdDoesntExist() {
        mockAuthentication(authenticationService, "titi", CONTRIBUTOR);

        newsFactRepository.save(createDefaultNewsFact());
        final int initialCount = newsFactRepository.findAll().size();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts/unexistingId", DELETE, HttpEntity.EMPTY, Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    public void deleteNewsFact_shouldThrowNotFoundIfLoggedUserIsNotOwner() {
        mockAuthentication(authenticationService, "mandela", CONTRIBUTOR);

        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setOwner("melenchon");
        newsFactRepository.save(newsFact);
        final int initialCount = newsFactRepository.findAll().size();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts/" + DEFAULT_NEWS_FACT_ID, DELETE, HttpEntity.EMPTY, Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenIdIsNull() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();

        NewsFactDetailDto newsFactDto = new NewsFactDetailDto.Builder()
                .id(null)
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .eventDate("eventDate")
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFactDto), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("A news fact to update must have an id!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenNewsCategoryIsMissing() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder()
                .newsCategoryId(null)
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFact), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenEventDateIsMissing() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();

        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder()
                .eventDate(null)
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFact), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Some data is invalid : Value 'null' is invalid for field 'eventDate'!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenAddressIsMissing() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder()
                .addressDetail(null)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFact), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Some data is invalid : Value 'null' is invalid for field 'addressDetail'!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenCityIsMissing() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder()
                .city(null)
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFact), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Some data is invalid : Value 'null' is invalid for field 'city'!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenCountryIsMissing() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder()
                .country(null)
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .city(DEFAULT_CITY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(DEFAULT_LOCATION_COORDINATE_X, DEFAULT_LOCATION_COORDINATE_Y))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFact), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Some data is invalid : Value 'null' is invalid for field 'country'!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenLocationCoordinateIsMissing() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFact = new NewsFactDetailDto.Builder()
                .locationCoordinate(null)
                .addressDetail(DEFAULT_ADDRESS_DETAIL)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFact), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Some data is invalid : Value 'null' is invalid for field 'locationCoordinate'!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowUnauthorizedWhenUserIsNotConnected() {
        mockAnonymous(authenticationService);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFactDto = createDefaultNewsFactDetailDto();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFactDto), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowForbiddenWhenConnectedUserIsNotContributor() {
        mockAuthentication(authenticationService, "admin", ADMIN);

        final int initialCount = newsFactRepository.findAll().size();
        NewsFactDetailDto newsFactDetailDto = createDefaultNewsFactDetailDto();

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFactDetailDto), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowNotFoundWhenNewsFactIdDoesntExist() {
        mockAuthentication(authenticationService, "coco", CONTRIBUTOR);

        newsCategoryRepository.save(createDefaultNewsCategory());
        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);
        final int initialCount = newsFactRepository.findAll().size();

        NewsFactDetailDto newsFactDetailDto = createDefaultNewsFactDetailDto();
        newsFactDetailDto.setId("unexistingId");
        newsFactDetailDto.setId(newsFact.getId());

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(newsFactDetailDto), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo("News fact with id 'existingId' was not found!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowNotFoundIfConnectedUserIsNotOwner() {
        mockAuthentication(authenticationService, "mandela", CONTRIBUTOR);

        newsCategoryRepository.save(createDefaultNewsCategory());
        NewsFact lulaNewsFact = createDefaultNewsFact();
        lulaNewsFact.setOwner("Lula");
        newsFactRepository.save(lulaNewsFact);
        final int initialCount = newsFactRepository.findAll().size();

        NewsFactDetailDto lulaNewsFactDto = createDefaultNewsFactDetailDto();
        lulaNewsFactDto.setId(lulaNewsFact.getId()); //The DTO and the Domain news facts share the same id

        ResponseEntity<Problem> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(lulaNewsFactDto), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo("News fact with id 'news_fact_id' was not found!");
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_caseOk() {
        mockAuthentication(authenticationService, "mandela", CONTRIBUTOR);

        String yesterdayString = "2020-05-01";
        String todayString = "2020-05-02";
        LocalDateTime today = LocalDate.parse(todayString).atStartOfDay();
        LocalDateTime yesterday = LocalDate.parse(yesterdayString).atStartOfDay();
        String newAddress = DEFAULT_ADDRESS_DETAIL + "-updated";
        String newCity = DEFAULT_CITY + "-updated";
        String newCountry = DEFAULT_COUNTRY + "-updated";
        LocationCoordinate newLocationCoordinate = new LocationCoordinate(3000.0, 700.0);
        NewsCategory oldNewsCategory = createDefaultNewsCategory1();
        NewsCategory newNewsCategory = createDefaultNewsCategory2();

        // Initialize the database
        final NewsFact initialNewsFact = createDefaultNewsFact();
        initialNewsFact.setCreatedDate(yesterday);
        initialNewsFact.setEventDate(yesterday);
        initialNewsFact.setOwner("mandela");
        newsFactRepository.save(initialNewsFact);
        newsCategoryRepository.save(oldNewsCategory);
        newsCategoryRepository.save(newNewsCategory);
        clockService.setClock(Clock.fixed(today.toInstant(ZoneOffset.UTC), ZoneId.of("Z"))); // to simulate creation today
        final int initialCount = newsFactRepository.findAll().size();

        //Given
        final NewsFactDetailDto toUpdateNewsFact = new NewsFactDetailDto.Builder()
                .addressDetail(newAddress)
                .city(newCity)
                .country(newCountry)
                .eventDate(todayString)
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(newLocationCoordinate)
                .newsCategoryId(newNewsCategory.getId())
                .build();

        // When
        ResponseEntity<NewsFactDetailDto> response = testRestTemplate.exchange("/newsFacts", PUT, new HttpEntity<>(toUpdateNewsFact), NewsFactDetailDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        NewsFactDetailDto resultNewsFact = response.getBody();
        assertThat(resultNewsFact.getAddressDetail()).isEqualTo(newAddress);
        assertThat(resultNewsFact.getCity()).isEqualTo(newCity);
        assertThat(resultNewsFact.getCountry()).isEqualTo(newCountry);
        assertThat(resultNewsFact.getCreatedDate()).isEqualTo(yesterdayString);
        assertThat(resultNewsFact.getEventDate()).isEqualTo(todayString);
        assertThat(resultNewsFact.getId()).isEqualTo(DEFAULT_NEWS_FACT_ID);
        assertThat(resultNewsFact.getLocationCoordinate().getX()).isEqualTo(newLocationCoordinate.getX());
        assertThat(resultNewsFact.getLocationCoordinate().getY()).isEqualTo(newLocationCoordinate.getY());
        assertThat(resultNewsFact.getNewsCategoryId()).isEqualTo(newNewsCategory.getId());
        assertThat(resultNewsFact.getNewsCategoryLabel()).isEqualTo(newNewsCategory.getLabel());

        // Validate the news fact in database
        List<NewsFact> newsFacts = newsFactRepository.findAll();
        assertThat(newsFacts).hasSize(initialCount);
        NewsFact updatedNewsFact = newsFacts.get(0);
        assertThat(updatedNewsFact.getLastModifiedDate()).isEqualTo(today);
        assertThat(updatedNewsFact.getOwner()).isEqualTo("mandela"); // Owner should not change
    }

    @Test
    public void getNewsFactMedia_shouldThrowNotFoundIfNewsFactIdDoesntExist() {
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/media/unexistingId", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody().getDetail()).isEqualTo("News fact with id 'unexistingId' was not found!");
    }

    @Test
    public void getNewsFactMedia_shouldThrowInternalServerErrorIfMediaFileDoesntExist() {

        //Init Database with a news fact wihout associated video file
        final NewsCategory newsCategory = createDefaultNewsCategory();
        newsCategoryRepository.save(newsCategory);
        final NewsFact newsFact = createDefaultNewsFact();
        newsFact.setMediaId("unexistingMediaId");
        newsFactRepository.save(newsFact);

        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/newsFacts/media/" + newsFact.getId(), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getDetail()).isEqualTo("Media of news fact with id 'news_fact_id' was not found!");
    }

    @Test
    public void getNewsFactMedia_caseOk() {

        //Init Database with news category, news fact and news fact media
        final NewsCategory newsCategory = createDefaultNewsCategory();
        newsCategoryRepository.save(newsCategory);

        final NewsFact newsFact = createDefaultNewsFact();

        InputStream mediaInputStream = new ByteArrayInputStream("media content".getBytes());
        String gridFsFilename = newsFact.getOwner() + "_" + DateUtil.DATE_TIME_FORMATTER.format(clockService.now()) + "_" + PNG.name();
        String mediaId = mediaGridFsTemplate.store(
                mediaInputStream,
                gridFsFilename,
                PNG.getContentType(),
                new Document().append(MediaService.OWNER_METADATA_KEY, newsFact.getOwner())).toString();

        newsFact.setMediaId(mediaId);
        newsFactRepository.save(newsFact);

        ResponseEntity<byte[]> response = testRestTemplate.getForEntity("/newsFacts/media/" + newsFact.getId(), byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo("media content".getBytes());
    }

    private int countGridFsVideos() {
        String newsFactVideoBucket = applicationProperties.getMongo().getGridFs().getNewsFactMediaBucket();
        MongoCollection<Document> videoFilesCollection = mongoTemplate.getCollection(newsFactVideoBucket + ".files");
        return (int) videoFilesCollection.countDocuments();
    }
}
