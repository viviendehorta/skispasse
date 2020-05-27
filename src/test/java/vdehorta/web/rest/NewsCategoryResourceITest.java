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
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.service.NewsCategoryService;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NewsCategoryResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class NewsCategoryResourceITest {

    public static final String DEFAULT_ID = "id";
    public static final String DEFAULT_LABEL = "label";

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private NewsCategoryService newsCategoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNewsCategoryMockMvc;


    @BeforeEach
    public void setup() {
        this.restNewsCategoryMockMvc = MockMvcBuilders.standaloneSetup(new NewsCategoryResource(newsCategoryService))
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    @BeforeEach
    public void initTest() {
        newsCategoryRepository.deleteAll();
    }

    @Test
    public void getAll_caseOk() throws Exception {

        // Initialize the database
        NewsCategory newsCategory1 = createBasicNewsCategory("1");
        NewsCategory newsCategory2 = createBasicNewsCategory("2");
        newsCategoryRepository.saveAll(Arrays.asList(newsCategory1, newsCategory2));

        ResultActions resultActions = restNewsCategoryMockMvc.perform(get("/newsCategories/all").accept(MediaType.APPLICATION_JSON));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(hasSize(2)))
            .andExpect(jsonPath("$[*].id").value(hasItems("id1", "id2")))
            .andExpect(jsonPath("$[*].label").value(hasItems("label1", "label2")));
    }



    /**
     * Create a NewsCategory entity with all fields initialized with fixed value, and string fields suffixed by the
     * given suffix.
     *
     * @param suffix the suffix to add at the end of NewsCategory's fields
     * @return The NewsCategory created
     */
    private NewsCategory createBasicNewsCategory(String suffix) {
        return new NewsCategory.Builder()
            .id(DEFAULT_ID + suffix)
            .label(DEFAULT_LABEL + suffix)
            .build();
    }
}
