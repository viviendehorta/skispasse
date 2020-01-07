package vdehorta.web.rest;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class NewsFactsResourceIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        NewsFactsResource newsFactsResource = new NewsFactsResource();
        this.mockMvc = MockMvcBuilders.standaloneSetup(newsFactsResource)
            .setControllerAdvice(exceptionTranslator)
            .build();
    }

    @Test
    @WithAnonymousUser()
    public void testGetNewsFactsBlobWithAnonymousUser() throws Exception {
        mockMvc.perform(post("/newsFacts/all")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }
}
