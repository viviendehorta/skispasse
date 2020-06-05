package vdehorta.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsFact;
import vdehorta.bean.dto.NewsFactDetailDto;
import vdehorta.bean.dto.NewsFactNoDetailDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static vdehorta.service.util.DateUtil.DATE_FORMATTER;

@Mapper(componentModel = "spring")
@Service
public interface NewsFactMapper {

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return DATE_FORMATTER.format(localDateTime);
    }

    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            return null;
        }
        return DATE_FORMATTER.parse(stringDate, LocalDate::from).atStartOfDay();
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

