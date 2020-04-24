package vdehorta.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Service;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;

import java.util.List;

@Mapper(componentModel = "spring")
@Service
public interface NewsFactMapper {

    @Mappings({
        @Mapping(source = "locationCoordinateX", target = "locationCoordinate.x"),
        @Mapping(source = "locationCoordinateY", target = "locationCoordinate.y"),
    })
    NewsFactDetailDto newsFactToNewsFactDetailDto(NewsFact newsFact);

    @Mappings({
        @Mapping(source = "locationCoordinateX", target = "locationCoordinate.x"),
        @Mapping(source = "locationCoordinateY", target = "locationCoordinate.y"),
    })
    NewsFactNoDetailDto newsFactToNewsFactNoDetailDto(NewsFact newsFact);

    List<NewsFactNoDetailDto> newsFactsToNewsFactNoDetailDtos(List<NewsFact> newsFact);
}

