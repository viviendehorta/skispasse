package vdehorta.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
@Service
public interface NewsFactMapper {

    DateTimeFormatter DATE_TO_STRING_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return DATE_TO_STRING_FORMATTER.format(localDateTime);
    }

    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            return null;
        }
        return DATE_TO_STRING_FORMATTER.parse(stringDate, LocalDate::from).atStartOfDay();
    }

    @Mappings({
        @Mapping(source = "locationCoordinateX", target = "locationCoordinate.x"),
        @Mapping(source = "locationCoordinateY", target = "locationCoordinate.y"),
        @Mapping(source = "eventDate", target="eventDate", qualifiedByName = "localDateTimeToString"),
        @Mapping(source = "createdDate", target="createdDate", qualifiedByName = "localDateTimeToString")
    })
    NewsFactDetailDto newsFactToNewsFactDetailDto(NewsFact newsFact);

    @Mappings({
        @Mapping(source = "locationCoordinateX", target = "locationCoordinate.x"),
        @Mapping(source = "locationCoordinateY", target = "locationCoordinate.y"),
    })
    NewsFactNoDetailDto newsFactToNewsFactNoDetailDto(NewsFact newsFact);

    List<NewsFactNoDetailDto> newsFactsToNewsFactNoDetailDtos(List<NewsFact> newsFact);

    @Mappings({
        @Mapping(source = "locationCoordinate.x", target = "locationCoordinateX"),
        @Mapping(source = "locationCoordinate.y", target = "locationCoordinateY"),
        @Mapping(source = "eventDate", target="eventDate", qualifiedByName = "stringToLocalDateTime"),
        @Mapping(source = "createdDate", target="createdDate", qualifiedByName = "stringToLocalDateTime")
    })
    NewsFact newsFactDetailDtoToNewsFact(NewsFactDetailDto newsFactDetailDto);
}

