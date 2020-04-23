package vdehorta.service.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.service.errors.UnexistingNewsCategoryException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link NewsFact} and its DTO called {@link NewsFact}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class NewsFactMapper {

    /* TODO ne plus avoir l'intelligence de chercher la news category dans ce mapper, le service doit passer
        la news category adaptée et ne pas requetter en BDD à chaque fois !! */
    public NewsFactDetailDto newsFactToNewsFactDetailDto(NewsFact newsFact, List<NewsCategoryDto> allNewsCategories) throws UnexistingNewsCategoryException {
        NewsFactDetailDto.Builder builder = new NewsFactDetailDto.Builder();
        NewsCategoryDto matchingNewsCategory = allNewsCategories.stream()
            .filter(newsCategory -> newsCategory.getId().equals(newsFact.getCategoryId()))
            .findFirst()
            .orElseThrow(() -> new UnexistingNewsCategoryException(newsFact.getCategoryId()));
        builder
            .address(newsFact.getAddress())
            .newsCategoryId(matchingNewsCategory.getId())
            .newsCategoryLabel(matchingNewsCategory.getLabel())
            .city(newsFact.getCity())
            .country(newsFact.getCountry())
            .eventDate(newsFact.getEventDate())
            .locationCoordinate(new LocationCoordinate(newsFact.getLocationCoordinateX(), newsFact.getLocationCoordinateY()))
            .id(newsFact.getId())
            .videoPath(newsFact.getVideoPath())
            .createdDate(newsFact.getCreatedDate());
        return builder.build();
    }

    public List<NewsFactNoDetailDto> newsFactsToNewsFactNoDetailDtos(List<NewsFact> newsFacts) {
        return newsFacts.stream().map(this::newsFactToNewsFactNoDetailDto).collect(Collectors.toList());
    }

    public NewsFactNoDetailDto newsFactToNewsFactNoDetailDto(NewsFact newsFact) {
        NewsFactNoDetailDto.Builder builder = new NewsFactNoDetailDto.Builder();
        builder
            .newsCategoryId(newsFact.getCategoryId())
            .locationCoordinate(new LocationCoordinate(newsFact.getLocationCoordinateX(), newsFact.getLocationCoordinateY()))
            .id(newsFact.getId());
        return builder.build();
    }

    public Page<NewsFactDetailDto> newsFactPageToNewsFactDetailDtoPage(Page<NewsFact> newsFactPage, List<NewsCategoryDto> allNewsCategories) {
        return newsFactPage.map(newsFact -> newsFactToNewsFactDetailDto(newsFact, allNewsCategories));
    }
}
