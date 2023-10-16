package com.vdehorta.skispasse.web.controller;

import com.vdehorta.skispasse.model.LonLat;
import com.vdehorta.skispasse.model.dto.NewsFactDto;
import com.vdehorta.skispasse.model.entity.NewsFactDocumentBuilder;
import com.vdehorta.skispasse.model.enums.ContentType;
import com.vdehorta.skispasse.model.enums.MediaType;
import com.vdehorta.skispasse.repository.NewsFactRepository;
import com.vdehorta.skispasse.util.MediaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsFactControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private NewsFactRepository newsFactRepository;

    @BeforeEach
    public void setup() {
        newsFactRepository.deleteAll();
    }

    @Test
    void list_shouldReturnAllNewsFacts() {

        //given
        LonLat lonLat = new LonLat(-3.7144507653840675, 40.41712531399446);
        LocalDateTime eventDate = LocalDateTime.of(2023, 10, 24, 14, 30);
        newsFactRepository.insert(
                NewsFactDocumentBuilder.aNewsFactDocument()
                        .withAddress("Palacio Real, Calle de Bailén, s/n, 28071 Madrid")
                        .withCategoryId("Manifestation")
                        .withEventDate(eventDate)
                        .withId("first-newsfact-id")
                        .withLocation(lonLat)
                        .withMediaContentType(ContentType.WEBM)
                        .withMediaId("media-id")
                        .withMediaType(MediaType.VIDEO)
                        .withPublisherId("publisher-id")
                        .withTitle("Manif étudiante à Paris")
                        .build());

        //when
        ResponseEntity<NewsFactDto[]> response = testRestTemplate.getForEntity(
                "/api/public/newsfact/list", NewsFactDto[].class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0])
                .returns("first-newsfact-id", from(NewsFactDto::id))
                .returns("Manif étudiante à Paris", from(NewsFactDto::title))
                .returns("Manifestation", from(NewsFactDto::categoryId))
                .returns(ContentType.WEBM, from(NewsFactDto::mediaContentType))
                .returns(MediaType.VIDEO, from(NewsFactDto::mediaType))
                .returns(MediaUtil.getMediaUrl("media-id"), from(NewsFactDto::mediaUri))
                .returns(lonLat, from(NewsFactDto::location))
                .returns(eventDate, from(NewsFactDto::eventDate))
                .returns("Palacio Real, Calle de Bailén, s/n, 28071 Madrid", from(NewsFactDto::address))
                .returns("publisher-id", from(NewsFactDto::publisherId))
        ;
    }
}
