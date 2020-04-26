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
import vdehorta.SkispasseApp;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;
import vdehorta.repository.NewsFactRepository;
import vdehorta.repository.UserRepository;
import vdehorta.service.NewsFactService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.ExceptionTranslator;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vdehorta.web.rest.EntityTestUtil.*;

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
        NewsFact newsFact1 = createSuffixFieldNewsFact("1");
        NewsFact newsFact2 = createSuffixFieldNewsFact("2");
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        // Call /newsFact/all controller method
        restNewsFactMockMvc.perform(get("/newsFact/all").accept(MediaType.APPLICATION_JSON))
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
        NewsFact newsFact = createNewsFact();
        newsFactRepository.save(newsFact);

        // Call /newsFact/{newsFactId} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/" + DEFAULT_NEWS_FACT_ID).accept(MediaType.APPLICATION_JSON));
        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is(DEFAULT_NEWS_FACT_ID)))
            .andExpect(jsonPath("$.address", is(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.city", is(DEFAULT_CITY)))
            .andExpect(jsonPath("$.country", is(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.createdDate", is(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.eventDate", is(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.newsCategoryId", is(DEFAULT_NEWS_CATEGORY_ID)))
            .andExpect(jsonPath("$.newsCategoryLabel", is(DEFAULT_NEWS_CATEGORY_LABEL)))
            .andExpect(jsonPath("$.videoPath", is(DEFAULT_VIDEO_PATH)));
    }

    @Test
    public void getById_shouldThrowExceptionWhenIdDoesNotExist() throws Exception {

        // Initialize the database
        User user = EntityTestUtil.createUser();

        //User is useless for this test but better to have a persisted user
        NewsFact newsFact = createNewsFact();
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);

        // Call /newsFact/{newsFactId} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/unexistingId").accept(MediaType.APPLICATION_JSON));
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-skispasseApp-error", "error.wrongNewsFactId"));
    }

    @Test
    public void getByOwner_caseOk() throws Exception {

        String poloLogin = "polo";

        // Initialize the database
        User poloUser = createUser();
        poloUser.setLogin(poloLogin);
        userRepository.save(poloUser);

        NewsFact newsFact1 = createSuffixFieldNewsFact("1");
        newsFact1.setOwner("marco");
        NewsFact newsFact2 = createSuffixFieldNewsFact("2");
        newsFact2.setOwner(poloLogin);
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        // Call /newsFact/contributor/{login} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/contributor/" + poloLogin).accept(MediaType.APPLICATION_JSON));
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
        User aUser = createUser();
        aUser.setLogin("aLogin");
        userRepository.save(aUser);

        // Call /newsFact/{newsFactId} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/contributor/unexisting_login").accept(MediaType.APPLICATION_JSON));
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-skispasseApp-error", "error.unexistingLogin"));
    }
}
