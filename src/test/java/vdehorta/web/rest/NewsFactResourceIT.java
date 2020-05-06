package vdehorta.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vdehorta.EntityTestUtil;
import vdehorta.SkispasseApp;
import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.repository.NewsFactRepository;
import vdehorta.repository.UserRepository;
import vdehorta.service.ClockService;
import vdehorta.service.NewsFactService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.ExceptionTranslator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vdehorta.EntityTestUtil.*;

/**
 * Integration tests for the {@link NewsFactResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class NewsFactResourceIT {

    @Autowired
    private NewsFactRepository newsFactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private NewsFactService newsFactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClockService clockService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restNewsFactMockMvc;


    @BeforeEach
    public void setup() {
        NewsFactResource newsFactResource = new NewsFactResource(newsFactService);

        this.restNewsFactMockMvc = MockMvcBuilders.standaloneSetup(newsFactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    @BeforeEach
    public void initTest() {
        newsFactRepository.deleteAll();
    }

    @Test
    public void getAll_caseOk() throws Exception {

        // Initialize the database
        NewsFact newsFact1 = createDefaultNewsFact1();
        NewsFact newsFact2 = createDefaultNewsFact2();
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        // Call /newsFact/all controller GET method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/all").accept(MediaType.APPLICATION_JSON));

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

        // Call /newsFact/{newsFactId} controller GET method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/" + newsFact.getId()).accept(MediaType.APPLICATION_JSON));

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
            .andExpect(jsonPath("$.newsCategoryLabel", is(newsFact.getNewsCategoryLabel())))
            .andExpect(jsonPath("$.videoPath", is(newsFact.getVideoPath())));
    }

    @Test
    public void getById_shouldThrowExceptionWhenIdDoesNotExist() throws Exception {

        // Initialize the database
        userRepository.save(EntityTestUtil.createDefaultUser1());

        //User is useless for this test but better to have a persisted user
        NewsFact newsFact = createDefaultNewsFact1();
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);

        // Call /newsFact/{newsFactId} controller GET method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/unexistingId").accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-skispasseApp-error", "error.wrongNewsFactId"));
    }

    @Test
    public void getByOwner_caseOk() throws Exception {

        String poloLogin = "polo";

        // Initialize the database
        User poloUser = createDefaultUser1();
        poloUser.setLogin(poloLogin);
        userRepository.save(poloUser);

        NewsFact newsFact1 = createDefaultNewsFact1();
        newsFact1.setOwner("marco");
        NewsFact newsFact2 = createDefaultNewsFact2();
        newsFact2.setOwner(poloLogin);
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        // Call /newsFact/contributor/{login} controller GET method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/contributor/" + poloLogin).accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is("news_fact_id2")))
            .andExpect(jsonPath("$[0].newsCategoryId", is(DEFAULT_NEWS_CATEGORY_ID + 2)));
    }

    @Test
    public void getByOwner_shouldThrowExceptionWhenLoginDoesNotExist() throws Exception {

        // Initialize the database
        User aUser = createDefaultUser1();
        aUser.setLogin("aLogin");
        userRepository.save(aUser);

        // Call /newsFact/{newsFactId} controller GET method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/contributor/unexisting_login").accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-skispasseApp-error", "error.unexistingLogin"));
    }

    @Test
    void createNewsFact_caseOk() throws Exception {

        // Init ClockService with a fix clock to be able to assert the date values
        LocalDateTime expectedNow = LocalDateTime.parse("2020-03-24T20:30:23");
        clockService.setClock(Clock.fixed(expectedNow.toInstant(ZoneOffset.UTC), ZoneId.of("Z"))); // "Z" for UTC time zone

        // Nothing to initialize in database
        NewsCategory newsCategory = createDefaultCategory1();
        newsCategoryRepository.save(newsCategory);

        //Given
        NewsFactDetailDto toCreateNewsFact = new NewsFactDetailDto.Builder()
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .eventDate(DEFAULT_DATE_FORMATTER.format(DEFAULT_EVENT_DATE))
            .locationCoordinate(new LocationCoordinate.Builder()
                .x(DEFAULT_LOCATION_COORDINATE_X)
                .y(DEFAULT_LOCATION_COORDINATE_Y)
                .build())
            .newsCategoryId(newsCategory.getId())
            .build();

        // When: Call /newsFact controller POST method
        ResultActions resultActions = restNewsFactMockMvc.perform(post("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toCreateNewsFact)));

        // Then
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
            .andExpect(jsonPath("$.newsCategoryLabel", is(newsCategory.getLabel())))
            .andExpect(jsonPath("$.videoPath", isEmptyOrNullString()));
    }

    @Test
    public void deleteNewsFact_caseOk() throws Exception {

        // Initialize the database
        newsFactRepository.save(createDefaultNewsFact());
        int databaseSizeBeforeDelete = newsFactRepository.findAll().size();

        // Then
        ResultActions resultActions = restNewsFactMockMvc.perform(delete("/newsFact/{id}", DEFAULT_NEWS_FACT_ID)
            .accept(TestUtil.APPLICATION_JSON_UTF8));

        // Assert
        resultActions
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<NewsFact> newsFacts = newsFactRepository.findAll();
        assertThat(newsFacts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    void update_shouldThrowExceptionWhenIdIsNull() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(header().string("X-skispasseApp-error", "error.idNull"));
    }

    @Test
    void update_shouldThrowExceptionWhenNewsCategoryIsMissing() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void update_shouldThrowExceptionWhenEventDateIsMissing() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void update_shouldThrowExceptionWhenAddressIsMissing() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void update_shouldThrowExceptionWhenCityIsMissing() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void update_shouldThrowExceptionWhenCountryIsMissing() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void update_shouldThrowExceptionWhenLocationCoordinateIsMissing() throws Exception {

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
        ResultActions resultActions = restNewsFactMockMvc.perform(put("/newsFact")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(noNewsCategoryNewsFact)));

        // Then
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }
}