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
import vdehorta.repository.NewsFactRepository;
import vdehorta.service.NewsFactService;
import vdehorta.web.rest.errors.ExceptionTranslator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NewsFactResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class NewsFactResourceIT {

    public static final Long DEFAULT_LOCATION_COORDINATE_X = 100L;
    public static final Long DEFAULT_LOCATION_COORDINATE_Y = 2000L;
    public static final Instant DEFAULT_CREATED_DATE = LocalDateTime.parse("2020-04-02T20:15:39").toInstant(ZoneOffset.UTC);
    public static final Instant DEFAULT_EVENT_DATE = LocalDateTime.parse("2020-04-01T00:00:00").toInstant(ZoneOffset.UTC);
    public static final String DEFAULT_ADDRESS = "address";
    public static final String DEFAULT_CITY = "city";
    public static final String DEFAULT_COUNTRY = "country";
    public static final String DEFAULT_ID = "id";
    public static final String DEFAULT_NEWS_CATEGORY_ID = "newsCategoryId";
    public static final String DEFAULT_NEWS_CATEGORY_LABEL = "newsCategoryLabel";
    public static final String DEFAULT_OWNER = "owner";
    public static final String DEFAULT_VIDEO_PATH = "videoPath";

    @Autowired
    private NewsFactRepository newsFactRepository;

    @Autowired
    private NewsFactService newsFactService;

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
        NewsFact newsFact1 = createBasicNewsFact("1");
        NewsFact newsFact2 = createBasicNewsFact("2");
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        // Call /newsFact/all controller method
        restNewsFactMockMvc.perform(get("/newsFact/all").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(hasSize(2)))
            .andExpect(jsonPath("$[*].id").value(hasItems("id1", "id2")))
            .andExpect(jsonPath("$[*].newsCategoryId").value(hasItems("newsCategoryId1", "newsCategoryId2")))
            .andExpect(jsonPath("$[*].locationCoordinate").value(hasSize(2)))
            .andExpect(jsonPath("$[*].locationCoordinate.x").value(hasItems(DEFAULT_LOCATION_COORDINATE_X.intValue(), DEFAULT_LOCATION_COORDINATE_X.intValue())))
            .andExpect(jsonPath("$[*].locationCoordinate.y").value(hasItems(DEFAULT_LOCATION_COORDINATE_Y.intValue(), DEFAULT_LOCATION_COORDINATE_Y.intValue())));
    }

    @Test
    public void getByOwner_caseOk() throws Exception {

        // Initialize the database
        NewsFact newsFact1 = createBasicNewsFact("1");
        newsFact1.setOwner("marco");
        NewsFact newsFact2 = createBasicNewsFact("2");
        newsFact2.setOwner("polo");
        newsFactRepository.saveAll(Arrays.asList(newsFact1, newsFact2));

        // Call /newsFact/contributor/{login} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/contributor/polo").accept(MediaType.APPLICATION_JSON));
        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is("id2")))
            .andExpect(jsonPath("$[0].newsCategoryId", is(DEFAULT_NEWS_CATEGORY_ID + 2)));
    }

    @Test
    public void getById_caseOk() throws Exception {

        // Initialize the database
        NewsFact newsFact1 = createBasicNewsFact("");
        newsFactRepository.save(newsFact1);

        // Call /newsFact/{newsFactId} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/id").accept(MediaType.APPLICATION_JSON));
        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is(DEFAULT_ID)))
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
    public void getById_shouldThrowExceptionWhenIdDoesnotExist() throws Exception {

        // Initialize the database
        NewsFact newsFact = createBasicNewsFact("");
        newsFact.setId("existingId");
        newsFactRepository.save(newsFact);

        // Call /newsFact/{newsFactId} controller method
        ResultActions resultActions = restNewsFactMockMvc.perform(get("/newsFact/unexistingId").accept(MediaType.APPLICATION_JSON));
        resultActions
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-skispasseApp-error", "error.wrongNewsFactId"));
    }

    /**
     * Create a News Fact entity with all fields initialized with fixed value, and string fields suffixed by the
     * given suffix.
     *
     * @param suffix the suffix to add at the end of NewsFact fields
     * @return The NewsFact created
     */
    private NewsFact createBasicNewsFact(String suffix) {
        return new NewsFact.Builder()
            .address(DEFAULT_ADDRESS + suffix)
            .city(DEFAULT_CITY + suffix)
            .country(DEFAULT_COUNTRY + suffix)
            .createdDate(DEFAULT_CREATED_DATE)
            .eventDate(DEFAULT_EVENT_DATE)
            .id(DEFAULT_ID + suffix)
            .locationCoordinateX(DEFAULT_LOCATION_COORDINATE_X)
            .locationCoordinateY(DEFAULT_LOCATION_COORDINATE_Y)
            .newsCategoryId(DEFAULT_NEWS_CATEGORY_ID + suffix)
            .newsCategoryLabel(DEFAULT_NEWS_CATEGORY_LABEL + suffix)
            .owner(DEFAULT_OWNER + suffix)
            .videoPath(DEFAULT_VIDEO_PATH + suffix)
            .build();
    }
}
