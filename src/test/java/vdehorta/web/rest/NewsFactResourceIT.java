package vdehorta.web.rest;

import org.hamcrest.text.StringContainsInOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vdehorta.SkispasseApp;
import vdehorta.web.rest.errors.ExceptionTranslator;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link NewsFactResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class NewsFactResourceIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        NewsFactResource newsFactResource = new NewsFactResource();
        this.mockMvc = MockMvcBuilders.standaloneSetup(newsFactResource)
            .setControllerAdvice(exceptionTranslator)
            .build();
    }

    @Test
    @WithAnonymousUser()
    public void testGetAllWithAnonymousUser() throws Exception {
        mockMvc.perform(post("/newsFacts/all")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //Check presence of mandatory characters in a json object list
            .andExpect(content().string(new StringContainsInOrder(Arrays.asList("[", "{", "}", "]"))));
    }

    @Test
    @WithAnonymousUser()
    public void testGetByIdWithAnonymousUser() throws Exception {
        mockMvc.perform(post("/newsFacts/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //Check presence of mandatory characters in a news fact json object
            .andExpect(content().string(new StringContainsInOrder(Arrays.asList("{", "\"id\"", "\"videoPath\"", "}"))));
    }
}
