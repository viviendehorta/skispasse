package vdehorta.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Problem;
import vdehorta.bean.dto.LocationInfoDto;
import vdehorta.config.ApplicationProperties;
import vdehorta.domain.MapStyle;
import vdehorta.repository.MapStyleRepository;
import vdehorta.service.MapsService;
import vdehorta.utils.PersistenceTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the {@link MapsResource} REST controller.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MapsResourceITest {

    @Autowired
    private MapStyleRepository mapStyleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;


    @BeforeEach
    public void setup() {
        PersistenceTestUtils.resetDatabase(mongoTemplate, applicationProperties);
    }

    @Test
    public void getMapStyle_caseOk() {

        MapStyle mapStyle = new MapStyle.Builder().id(MapsService.MAP_STYLE_ID).json("fake json").build();
        mapStyleRepository.insert(mapStyle);

        ResponseEntity<String> response = testRestTemplate.getForEntity("/maps/map-style", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("fake json");
    }

    @Test
    public void getMapStyle_shouldThrowInternalServerErrorIfNoMapStyleExists() {
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/maps/map-style", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getDetail()).isEqualTo("Error getting application map style!");
    }

    @Test
    public void getLocationInfo_caseOk() {
        ResponseEntity<LocationInfoDto> response = testRestTemplate.getForEntity(
                "/maps/location-info?longitude=2.4776271235391145&latitude=48.89515222739624", LocationInfoDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCountry()).isEqualTo("France");
        assertThat(response.getBody().getCity()).isEqualTo("Bondy");
        assertThat(response.getBody().getLocality()).isEqualTo("Bondy");
    }
}
