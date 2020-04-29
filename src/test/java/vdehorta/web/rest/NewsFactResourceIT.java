package vdehorta.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
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
import vdehorta.service.NewsFactService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.ExceptionTranslator;

import java.time.Instant;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            .andExpect(jsonPath("$.createdDate", is(newsFact.getCreatedDate().toString())))
            .andExpect(jsonPath("$.eventDate", is(newsFact.getEventDate().toString())))
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

        // Nothing to initialize in database
        NewsCategory newsCategory = createDefaultCategory1();
        newsCategoryRepository.save(newsCategory);

        //Given
        NewsFactDetailDto toCreateNewsFact = new NewsFactDetailDto.Builder()
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .eventDate(DEFAULT_EVENT_DATE)
            .locationCoordinate(new LocationCoordinate.Builder()
                .x(DEFAULT_LOCATION_COORDINATE_Y)
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
            .andExpect(jsonPath("$.id", isA(String.class)))
            .andExpect(jsonPath("$.address", is(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.city", is(DEFAULT_CITY)))
            .andExpect(jsonPath("$.country", is(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.createdDate", isA(String.class)))
            .andExpect(jsonPath("$.eventDate", is(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.newsCategoryId", is(newsCategory.getId())))
            .andExpect(jsonPath("$.newsCategoryLabel", is(newsCategory.getLabel())))
            .andExpect(jsonPath("$.videoPath", isEmptyOrNullString()));
    }
}
