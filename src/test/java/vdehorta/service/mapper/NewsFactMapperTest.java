package vdehorta.service.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import vdehorta.domain.NewsFact;
import vdehorta.bean.dto.NewsFactDetailDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NewsFactMapperTest {

    NewsFactMapper newsFactMapper = Mappers.getMapper(NewsFactMapper.class);

    @Test
    void newsFactToNewsFactDetailDto_shouldConvertDatesToStringFormat() {
        NewsFact input = new NewsFact.Builder()
            .createdDate(LocalDateTime.parse("2020-04-02T00:00:00"))
            .eventDate(LocalDateTime.parse("2020-04-01T00:00:00"))
            .build();

        NewsFactDetailDto result = newsFactMapper.newsFactToNewsFactDetailDto(input);

        assertThat(result.getCreatedDate()).isEqualTo("2020-04-02");
        assertThat(result.getEventDate()).isEqualTo("2020-04-01");
    }

    @Test
    void newsFactToNewsFactDetailDto_shouldConvertXYLocationCoordinateFieldsToLocationCoordinateObject() {
        NewsFact input = new NewsFact.Builder()
            .locationCoordinateX(10.0)
            .locationCoordinateY(2.0)
            .build();

        NewsFactDetailDto result = newsFactMapper.newsFactToNewsFactDetailDto(input);

        assertThat(result.getLocationCoordinate().getX()).isEqualTo(10L);
        assertThat(result.getLocationCoordinate().getY()).isEqualTo(2L);
    }

    @Test
    void newsFactDetailDtoToNewsFact_shouldConvertStringDatesToInstant() {
        NewsFactDetailDto input = new NewsFactDetailDto.Builder()
            .createdDate("2020-04-02")
            .eventDate("2020-04-01")
            .build();

        NewsFact result = newsFactMapper.newsFactDetailDtoToNewsFact(input);

        assertThat(result.getCreatedDate()).isEqualTo(LocalDateTime.parse("2020-04-02T00:00:00"));
        assertThat(result.getEventDate()).isEqualTo(LocalDateTime.parse("2020-04-01T00:00:00"));
    }

    @Test
    void newsFactToNewsFactDetailDto_shouldMapMediaContentTypeValue() {
        NewsFact input = new NewsFact.Builder()
                .mediaContentType("mediaContentType")
                .build();

        NewsFactDetailDto result = newsFactMapper.newsFactToNewsFactDetailDto(input);

        assertThat(result.getMediaContentType()).isEqualTo("mediaContentType");
    }
}
