package vdehorta.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import vdehorta.bean.ContentTypeEnum;
import vdehorta.bean.MediaType;
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
    static LocalDateTime stringToLocalDateTime(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return DATE_FORMATTER.parse(dateString, LocalDate::from).atStartOfDay();
    }

    @Named("stringToMediaType")
    static MediaType stringToMediaType(String mediaTypeString) {
        if (mediaTypeString == null || mediaTypeString.isEmpty()) {
            return null;
        }
        return MediaType.getByValue(mediaTypeString).orElse(null);
    }

    @Named("stringToContentType")
    static ContentTypeEnum stringToContentType(String contentTypeString) {
        if (contentTypeString == null || contentTypeString.isEmpty()) {
            return null;
        }
        return ContentTypeEnum.getByContentType(contentTypeString).orElse(null);
    }

    @Mappings({
        @Mapping(source = "locationCoordinateX", target = "locationCoordinate.x"),
        @Mapping(source = "locationCoordinateY", target = "locationCoordinate.y"),
        @Mapping(source = "eventDate", target="eventDate", qualifiedByName = "localDateTimeToString"),
        @Mapping(source = "createdDate", target="createdDate", qualifiedByName = "localDateTimeToString"),
        @Mapping(source = "mediaType", target="media.type", qualifiedByName = "stringToMediaType"),
        @Mapping(source = "mediaContentType", target="media.contentType", qualifiedByName = "stringToMediaContentType")
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

