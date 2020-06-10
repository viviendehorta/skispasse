package vdehorta.web.rest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vdehorta.EntityTestUtil;
import vdehorta.SkispasseApp;
import vdehorta.bean.dto.NewsFactDetailDto;
import vdehorta.config.ApplicationProperties;
import vdehorta.converter.JacksonMapperFactory;
import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.repository.NewsFactRepository;
import vdehorta.repository.UserRepository;
import vdehorta.service.AuthenticationService;
import vdehorta.service.ClockService;
import vdehorta.service.NewsFactService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.ExceptionTranslator;

import java.time.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vdehorta.EntityTestUtil.*;
import static vdehorta.bean.ContentTypeEnum.MP4;

/**
 * Integration tests for the {@link NewsFactResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class NewsFactResourceITest {

    @Autowired
    private NewsFactService newsFactService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

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
    private GridFsTemplate videoGridFsTemplate;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restNewsFactMockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;


    @BeforeEach
    public void setup() {

        NewsFactResource newsFactResource = new NewsFactResource(applicationProperties, newsFactService, authenticationService);

        this.restNewsFactMockMvc = MockMvcBuilders.standaloneSetup(newsFactResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @BeforeEach
    public void initTest() {
        TestUtil.resetDatabase(mongoTemplate);
    }

    @Test
    public void getAll_caseOk() throws Exception {

        // Initialize the database
        NewsFact newsFact1 = createDefaultNewsFact1();
        NewsFact newsFact2 = createDefaultNewsFact2();
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/all").accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[*].id").value(hasItems("news_fact_id1", "news_fact_id2")))
                .andExpect(jsonPath("$[*].newsCategoryId").value(hasItems("newsCategoryId1", "newsCategoryId2")))
                .andExpect(jsonPath("$[*].locationCoordinate").value(hasSize(2)))
                .andExpect(jsonPath("$[*].locationCoordinate.x").value(hasItems(DEFAULT_LOCATION_COORDINATE_X.intValue(), DEFAULT_LOCATION_COORDINATE_X.intValue())))
                .andExpect(jsonPath("$[*].locationCoordinate.y").value(hasItems(DEFAULT_LOCATION_COORDINATE_Y.intValue(), DEFAULT_LOCATION_COORDINATE_Y.intValue())));
    }

    @Test
    public void getById_caseOk() throws Exception {

        // Initialize the database
        NewsFact newsFact = createDefaultNewsFact1();
        newsFact.setCreatedDate(LocalDateTime.parse("2020-05-01T23:30:00"));
        newsFact.setEventDate(LocalDateTime.parse("2020-02-28T22:30:00"));
        newsFactRepository.save(newsFact);

        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/" + newsFact.getId()).accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(newsFact.getId())))
                .andExpect(jsonPath("$.address", is(newsFact.getAddress())))
                .andExpect(jsonPath("$.city", is(newsFact.getCity())))
                .andExpect(jsonPath("$.country", is(newsFact.getCountry())))
                .andExpect(jsonPath("$.createdDate", is("2020-05-01")))
                .andExpect(jsonPath("$.eventDate", is("2020-02-28")))
                .andExpect(jsonPath("$.newsCategoryId", is(newsFact.getNewsCategoryId())))
                .andExpect(jsonPath("$.newsCategoryLabel", is(newsFact.getNewsCategoryLabel())));
    }

    @Test
    public void getById_shouldThrowNotFoundIfGivenIdDoesNotExist() throws Exception {

        // Initialize the database
        userRepository.save(EntityTestUtil.createDefaultUser1());

        //User is useless for this test but better to have a persisted user
        NewsFact newsFact = createDefaultNewsFact1();
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);

        // Then
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/unexistingId").accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("News fact with id 'unexistingId' was not found!")));
    }

    @Test
    @WithMockUser(username = "Polo", roles = {"CONTRIBUTOR"})
    public void getMyNewsFacts_caseOk() throws Exception {

        String poloLogin = "Polo";

        // Initialize the database
        User poloUser = createDefaultUser1();
        poloUser.setLogin(poloLogin);
        userRepository.save(poloUser);

        NewsFact newsFact1 = createDefaultNewsFact1();
        newsFact1.setOwner("marco");
        NewsFact newsFact2 = createDefaultNewsFact2();
        newsFact2.setOwner(poloLogin);
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/contributor").accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("news_fact_id2")))
                .andExpect(jsonPath("$[0].newsCategoryId", is(DEFAULT_NEWS_CATEGORY_ID + 2)));
    }

    @Test
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsNotAuthenticated() throws Exception {
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/contributor").accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "skisp", roles = {"ADMIN"})
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsOnlyAdmin() throws Exception {
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/contributor").accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "skisp", roles = {"USER"})
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsOnlyUser() throws Exception {
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/contributor").accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "skisp", roles = {"USER", "ADMIN"})
    public void getMyNewsFacts_shouldThrowExceptionWhenUserIsAdminAndUser() throws Exception {
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/contributor").accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "zeus", roles = {"USER", "CONTRIBUTOR"})
    void createNewsFact_caseOk() throws Exception {

        String videoFilePath = "skispasse.mp4";

        // Init ClockService with a fix clock to be able to assert the date values
        LocalDateTime fixedNow = LocalDateTime.parse("2020-03-24T20:30:23");
        clockService.setClock(Clock.fixed(fixedNow.toInstant(ZoneOffset.UTC), ZoneId.of("Z"))); // "Z" for UTC time zone

        // Initialize database
        NewsCategory newsCategory = createDefaultNewsCategory1();
        newsCategoryRepository.save(newsCategory);
        final int newsFactInitialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto toCreateNewsFact = EntityTestUtil.createDefaultNewsFactDetailDto();
        toCreateNewsFact.setId(null);
        toCreateNewsFact.setNewsCategoryId(newsCategory.getId());
        toCreateNewsFact.setNewsCategoryLabel(null);

        MockMultipartFile videoMultiPartFile = new MockMultipartFile("videoFile", videoFilePath, MP4.getContentType(), "video file content".getBytes());
        String newsFactJson = TestUtil.convertObjectToJsonString(toCreateNewsFact);

        ResultActions resultActions = restNewsFactMockMvc.perform(multipart("/newsFacts")
                .file(videoMultiPartFile)
                .param("newsFactJson", newsFactJson));

        //Check HTTP response
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.address", is(DEFAULT_ADDRESS)))
                .andExpect(jsonPath("$.city", is(DEFAULT_CITY)))
                .andExpect(jsonPath("$.country", is(DEFAULT_COUNTRY)))
                .andExpect(jsonPath("$.createdDate", is("2020-03-24")))
                .andExpect(jsonPath("$.eventDate", is(DEFAULT_DATE_FORMATTER.format(DEFAULT_EVENT_DATE))))
                .andExpect(jsonPath("$.id", isA(String.class)))
                .andExpect(jsonPath("$.locationCoordinate.x", is(DEFAULT_LOCATION_COORDINATE_X.intValue())))
                .andExpect(jsonPath("$.locationCoordinate.y", is(DEFAULT_LOCATION_COORDINATE_Y.intValue())))
                .andExpect(jsonPath("$.newsCategoryId", is(newsCategory.getId())))
                .andExpect(jsonPath("$.newsCategoryLabel", is(newsCategory.getLabel())));

        //Check news fact persistence
        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount + 1);
        NewsFact persistedNewsFact = newsFactRepository.findById(parseNewsFactDetailJson(resultActions.andReturn().getResponse().getContentAsString()).getId()).orElse(null);
        assertThat(persistedNewsFact).isNotNull();
        assertThat(persistedNewsFact.getAddress()).isEqualTo(DEFAULT_ADDRESS);
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

        //Check video file persistence
        MongoCursor<GridFSFile> persistedVideoCursor = videoGridFsTemplate.find(new Query().addCriteria(Criteria.where("_id").is(persistedNewsFact.getMediaId()))).iterator();
        assertThat(persistedVideoCursor.hasNext()).isTrue();

        GridFSFile persistedVideoFile = persistedVideoCursor.next();
        assertThat(persistedVideoCursor.hasNext()).isFalse(); //Check there is only 1 matching file

        assertThat(persistedVideoFile.getFilename()).contains("zeus"); //Owner login should be part of the filename
        assertThat(persistedVideoFile.getUploadDate()).isNotNull(); //TODO : set that using the clockService in code to be able to test it
        assertThat(persistedVideoFile.getMetadata().getString("owner")).isEqualTo("zeus");
    }

    @Test
    @WithMockUser(username = "toto", roles = {"USER", "ADMIN"})
    void createNewsFact_shouldThrowExceptionForNoneContributorUser() throws Exception {

        // Initialize database
        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto newsFactDetailDto = new NewsFactDetailDto.Builder().build();

        MockMultipartFile videoMultiPartFile = new MockMultipartFile("videoFile", "skispasse.mp4", "video/mp4", "video file content".getBytes());
        String newsFactJson = TestUtil.convertObjectToJsonString(newsFactDetailDto);

        ResultActions resultActions = restNewsFactMockMvc.perform(multipart("/newsFacts")
                .file(videoMultiPartFile)
                .param("newsFactJson", newsFactJson));

        // Then
        resultActions.andExpect(status().isForbidden());
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    //TODO à terminer dans SKIS-141
//    @Test
//    @WithMockUser(username = "ares", roles = {"USER", "CONTRIBUTOR"})
//    void createNewsFact_shouldThrowErrorIfVideoFileIsTooBig() throws Exception {
//
//        String tooBigFilename = "tooBigVideo.mp4";
//
//        //Given
//        NewsFactDetailDto toCreateNewsFact = EntityTestUtil.createDefaultNewsFactDetailDto();
//        toCreateNewsFact.setId(null);
//
//        byte[] tooBigFileBytes = new byte[50 * 1024]; //100KB file content
//        Arrays.fill(tooBigFileBytes, (byte) 1);
//        MockMultipartFile tooBigFile = new MockMultipartFile("videoFile", tooBigFilename, MP4.getContentType(), tooBigFileBytes);
//        String newsFactJson = TestUtil.convertObjectToJsonString(toCreateNewsFact);
//
//        // Initialize database
//        final int newsFactInitialCount = newsFactRepository.findAll().size();
//        final int videoInitialCount = countGridFsVideos();
//
//        //When
//        ResultActions resultActions = restNewsFactMockMvc.perform(multipart("/newsFacts")
//                .file(tooBigFile)
//                .param("newsFactJson", newsFactJson));
//
//        //Then
//        resultActions.andExpect(status().isUnsupportedMediaType());
//        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount);
//        assertThat(countGridFsVideos()).isEqualTo(videoInitialCount);
//    }

    @Test
    @WithMockUser(username = "ares", roles = {"USER", "CONTRIBUTOR"})
    void createNewsFact_shouldNotPersistNewsFactIfErrorOccursPersistingVideo() throws Exception {

        String textFilePath = "skispasse.txt"; //Text file to throw error when persisting file

        // Initialize database
        NewsCategory newsCategory = createDefaultNewsCategory1();
        newsCategoryRepository.save(newsCategory);
        final int newsFactInitialCount = newsFactRepository.findAll().size();
        final int videoInitialCount = countGridFsVideos();

        //Given
        NewsFactDetailDto toCreateNewsFact = EntityTestUtil.createDefaultNewsFactDetailDto();
        toCreateNewsFact.setId(null);
        toCreateNewsFact.setNewsCategoryId(newsCategory.getId());
        toCreateNewsFact.setNewsCategoryLabel(null);

        MockMultipartFile textMultiPartFile = new MockMultipartFile("videoFile", textFilePath, "text/plain", "text content".getBytes());
        String newsFactJson = TestUtil.convertObjectToJsonString(toCreateNewsFact);

        //When
        ResultActions resultActions = restNewsFactMockMvc.perform(multipart("/newsFacts")
                .file(textMultiPartFile)
                .param("newsFactJson", newsFactJson));

        //Then
        resultActions.andExpect(status().isUnsupportedMediaType());
        assertThat(newsFactRepository.findAll()).hasSize(newsFactInitialCount);
        assertThat(countGridFsVideos()).isEqualTo(videoInitialCount);
    }

    @Test
    @WithMockUser(username = "titi", roles = {"CONTRIBUTOR"})
    public void deleteNewsFact_caseOk() throws Exception {

        // Initialize the database
        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setOwner("titi");
        newsFactRepository.save(newsFact);
        int initialCount = newsFactRepository.findAll().size();

        // Then
        ResultActions resultActions = restNewsFactMockMvc.perform(delete("/newsFacts/{id}", DEFAULT_NEWS_FACT_ID)
                .accept(APPLICATION_JSON_UTF8));

        // Assert
        resultActions
                .andExpect(status().isNoContent());

        // Validate the database is empty
        List<NewsFact> newsFacts = newsFactRepository.findAll();
        assertThat(newsFacts).hasSize(initialCount - 1);
    }

    @Test
    @WithMockUser(username = "titi", roles = {"CONTRIBUTOR"})
    public void deleteNewsFact_shouldThrowNotFoundIfGivenIdDoesntExist() throws Exception {

        newsFactRepository.save(createDefaultNewsFact());
        final int initialCount = newsFactRepository.findAll().size();

        ResultActions resultActions = restNewsFactMockMvc.perform(delete("/newsFacts/{id}", "unexistingId")
                .accept(APPLICATION_JSON_UTF8));

        resultActions.andExpect(status().isNotFound());

        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(username = "mandela", roles = {"CONTRIBUTOR"})
    public void deleteNewsFact_shouldThrowNotFoundIfLoggedUserIsNotOwner() throws Exception {

        // Initialize the database
        NewsFact newsFact = createDefaultNewsFact();
        newsFact.setOwner("melenchon");
        newsFactRepository.save(newsFact);
        final int initialCount = newsFactRepository.findAll().size();

        // Then
        ResultActions resultActions = restNewsFactMockMvc.perform(delete("/newsFacts/{id}", DEFAULT_NEWS_FACT_ID)
                .accept(APPLICATION_JSON_UTF8));

        // Assert
        resultActions.andExpect(status().isNotFound());

        // Validate the database is empty
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(roles = {"CONTRIBUTOR"})
    void update_shouldThrowExceptionWhenIdIsNull() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .id(null)
                .address(DEFAULT_ADDRESS)
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .eventDate("eventDate")
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .locationCoordinate(new LocationCoordinate(1L, 1L))
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("A news fact to update must have an id!")));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenNewsCategoryIsMissing() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .newsCategoryId(null)
                .address(DEFAULT_ADDRESS)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(1L, 1L))
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenEventDateIsMissing() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .eventDate(null)
                .address(DEFAULT_ADDRESS)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(1L, 1L))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenAddressIsMissing() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .address(null)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(1L, 1L))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenCityIsMissing() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .city(null)
                .address(DEFAULT_ADDRESS)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(1L, 1L))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenCountryIsMissing() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .country(null)
                .address(DEFAULT_ADDRESS)
                .city(DEFAULT_CITY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(new LocationCoordinate(1L, 1L))
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowExceptionWhenLocationCoordinateIsMissing() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto noNewsCategoryNewsFact = new NewsFactDetailDto.Builder()
                .locationCoordinate(null)
                .address(DEFAULT_ADDRESS)
                .city(DEFAULT_CITY)
                .country(DEFAULT_COUNTRY)
                .eventDate("eventDate")
                .id(DEFAULT_NEWS_FACT_ID)
                .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID)
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    void update_shouldThrowUnauthorizedWhenUserIsNotConnected() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto newsFactDetailDto = EntityTestUtil.createDefaultNewsFactDetailDto();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(newsFactDetailDto)));

        // Then
        resultActions.andExpect(status().isUnauthorized());
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void update_shouldThrowForbiddenWhenConnectedUserIsNotContributor() throws Exception {

        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto newsFactDetailDto = EntityTestUtil.createDefaultNewsFactDetailDto();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(newsFactDetailDto)));

        // Then
        resultActions.andExpect(status().isForbidden());
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(roles = {"CONTRIBUTOR"})
    void update_shouldThrowNotFoundWhenNewsFactIdDoesntExist() throws Exception {

        //Initialize database
        newsCategoryRepository.save(EntityTestUtil.createDefaultNewsCategory());
        NewsFact newsFact = EntityTestUtil.createDefaultNewsFact();
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);
        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto newsFactDetailDto = EntityTestUtil.createDefaultNewsFactDetailDto();
        newsFactDetailDto.setId("unexistingId");
        newsFactDetailDto.setId(newsFact.getId());

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(newsFactDetailDto)));

        // Then
        resultActions.andExpect(status().isNotFound());
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(username = "mandela", roles = {"CONTRIBUTOR"})
    void update_shouldThrowNotFoundIfConnectedUserIsNotOwner() throws Exception {

        //Initialize database
        newsCategoryRepository.save(EntityTestUtil.createDefaultNewsCategory());
        NewsFact lulaNewsFact = EntityTestUtil.createDefaultNewsFact();
        lulaNewsFact.setOwner("Lula");
        newsFactRepository.save(lulaNewsFact);
        final int initialCount = newsFactRepository.findAll().size();

        //Given
        NewsFactDetailDto lulaNewsFactDto = EntityTestUtil.createDefaultNewsFactDetailDto();
        lulaNewsFactDto.setId(lulaNewsFact.getId()); //The DTO and the Domain news facts share the same id

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lulaNewsFactDto)));

        // Then
        resultActions.andExpect(status().isNotFound());
        assertThat(newsFactRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(username = "mandela", roles = {"CONTRIBUTOR"})
    void update_caseOk() throws Exception {
        String yesterdayString = "2020-05-01";
        String todayString = "2020-05-02";
        LocalDateTime today = LocalDate.parse(todayString).atStartOfDay();
        LocalDateTime yesterday = LocalDate.parse(yesterdayString).atStartOfDay();
        String newAddress = DEFAULT_ADDRESS + "-updated";
        String newCity = DEFAULT_CITY + "-updated";
        String newCountry = DEFAULT_COUNTRY + "-updated";
        LocationCoordinate newLocationCoordinate = new LocationCoordinate.Builder().x(2000L).y(700L).build();
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
                .address(newAddress)
                .city(newCity)
                .country(newCountry)
                .eventDate(todayString)
                .id(DEFAULT_NEWS_FACT_ID)
                .locationCoordinate(newLocationCoordinate)
                .newsCategoryId(newNewsCategory.getId())
                .build();

        // When
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFacts")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toUpdateNewsFact)));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.address", is(newAddress)))
                .andExpect(jsonPath("$.city", is(newCity)))
                .andExpect(jsonPath("$.country", is(newCountry)))
                .andExpect(jsonPath("$.createdDate", is(yesterdayString)))
                .andExpect(jsonPath("$.eventDate", is(todayString)))
                .andExpect(jsonPath("$.id", is(DEFAULT_NEWS_FACT_ID)))
                .andExpect(jsonPath("$.locationCoordinate.x", is(newLocationCoordinate.getX().intValue())))
                .andExpect(jsonPath("$.locationCoordinate.y", is(newLocationCoordinate.getY().intValue())))
                .andExpect(jsonPath("$.newsCategoryId", is(newNewsCategory.getId())))
                .andExpect(jsonPath("$.newsCategoryLabel", is(newNewsCategory.getLabel())));

        // Validate the news fact in database
        List<NewsFact> newsFacts = newsFactRepository.findAll();
        assertThat(newsFacts).hasSize(initialCount);
        NewsFact updatedNewsFact = newsFacts.get(0);
        assertThat(updatedNewsFact.getLastModifiedDate()).isEqualTo(today);
        assertThat(updatedNewsFact.getOwner()).isEqualTo("mandela"); // Owner should not change
    }

    @Test
    public void streamNewsFactVideo_shouldThrowNotFoundIfNewsFactIdDoesntExist() throws Exception {
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/video/unexistingId"));
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", is("News fact with id 'unexistingId' was not found!")));
    }

    @Test
    public void streamNewsFactVideo_shouldThrowInternalServerErrorIfVideoFileDoesntExist() throws Exception {

        //Init Database with a news fact wihout associated video file
        final NewsCategory newsCategory = createDefaultNewsCategory();
        newsCategoryRepository.save(newsCategory);
        final NewsFact newsFact = createDefaultNewsFact();
        newsFact.setMediaId("unexistingMediaId");
        newsFactRepository.save(newsFact);

        //Given
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFacts/video/" + newsFact.getId()));

        //Then
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail", is("Error while accessing video of news fact with id '" + newsFact.getId() + "'!")));
    }

    private NewsFactDetailDto parseNewsFactDetailJson(String json) throws java.io.IOException {
        return JacksonMapperFactory.getObjectMapper().readValue(json, NewsFactDetailDto.class);
    }

    private int countGridFsVideos() {
        String newsFactVideoBucket = applicationProperties.getMongo().getGridFs().getNewsFactVideoBucket();
        MongoCollection<Document> videoFilesCollection = mongoTemplate.getCollection(newsFactVideoBucket + ".files");
        return (int) videoFilesCollection.countDocuments();
    }
}
