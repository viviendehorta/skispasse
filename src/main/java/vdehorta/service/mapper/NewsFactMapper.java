package vdehorta.service.mapper;

import org.springframework.stereotype.Service;
import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link NewsFact} and its DTO called {@link NewsFact}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class NewsFactMapper {

    public NewsFactDetailDto newsFactToNewsFactDetailDto(NewsFact newsFact) {
        NewsFactDetailDto.Builder builder = new NewsFactDetailDto.Builder();
        builder
            .address(newsFact.getAddress())
            .newsCategoryId(newsFact.getCategoryId())
            .city(newsFact.getCity())
            .country(newsFact.getCountry())
            .eventDate(newsFact.getEventDate())
            .geoCoordinate(new LocationCoordinate(newsFact.getGeoCoordinateX(), newsFact.getGeoCoordinateY()))
            .id(newsFact.getId())
            .videoPath(newsFact.getVideoPath());
        return builder.build();
    }

    public List<NewsFactDetailDto> newsFactsToNewsFactDetailDtos(List<NewsFact> newsFacts) {
        return newsFacts.stream()
            .map(this::newsFactToNewsFactDetailDto).collect(Collectors.toList());
    }
}
