package vdehorta.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import vdehorta.bean.dto.NewsCategoryDto;
import vdehorta.repository.NewsCategoryRepository;
import vdehorta.utils.BeanTestUtils;
import vdehorta.utils.PersistenceTestUtils;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Integration tests for the {@link NewsCategoryResource} REST controller.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NewsCategoryResourceITest {

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setup(@Autowired TestRestTemplate testRestTemplate) {
        PersistenceTestUtils.resetDatabase(mongoTemplate);
        this.testRestTemplate = testRestTemplate;
    }

    @Test
    public void getAll_caseOk() {
        newsCategoryRepository.saveAll(Arrays.asList(BeanTestUtils.createDefaultNewsCategory1(), BeanTestUtils.createDefaultNewsCategory2()));

        ResponseEntity<NewsCategoryDto[]> response = testRestTemplate.getForEntity("/newsCategories/all", NewsCategoryDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertThat(response.getBody())
                .hasSize(2)
                .extracting("id", "label").contains(tuple("newsCategoryId1", "newsCategoryLabel1"), tuple("newsCategoryId2", "newsCategoryLabel2"));
    }
}
