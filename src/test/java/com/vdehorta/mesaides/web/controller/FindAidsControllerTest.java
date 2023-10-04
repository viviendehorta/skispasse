package com.vdehorta.mesaides.web.controller;

import com.vdehorta.mesaides.model.dto.FindAidsResultDto;
import com.vdehorta.mesaides.model.dto.PersonalDetailDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.vdehorta.mesaides.TestUtil.buildJsonEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FindAidsControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void findAids_shouldReturnEmptyResult() {

        //given
        PersonalDetailDto personalDetailDto = new PersonalDetailDto("93140");

        //when
        ResponseEntity<FindAidsResultDto> response = testRestTemplate.postForEntity(
                "/api/public/find-aids", buildJsonEntity(personalDetailDto), FindAidsResultDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().aidsByCategory()).containsKey("Logement");
    }
}
