package vdehorta.repository;

import vdehorta.bean.LocationCoordinate;
import vdehorta.bean.NewsCategory;
import vdehorta.bean.NewsFactDetail;
import vdehorta.bean.NewsFactNoDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewsFactRepository {

    private static final long BASE_COORD = 4500000L;

    private final NewsFactNoDetail.Builder NEWS_FACT_NO_DETAIL_BUILDER = new NewsFactNoDetail.Builder();

    protected final List<NewsFactNoDetail> ALL_NEWS_FACTS_NO_DETAIL = Arrays.asList(
        NEWS_FACT_NO_DETAIL_BUILDER.id(1).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD)).category(NewsCategory.CULTURE).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(2).locationCoordinate(new LocationCoordinate(BASE_COORD / 2, BASE_COORD)).category(NewsCategory.DEMONSTRATION).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(3).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD / 3)).category(NewsCategory.NATURE).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(4).locationCoordinate(new LocationCoordinate(BASE_COORD / 4, BASE_COORD / 4)).category(NewsCategory.OTHER).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(5).locationCoordinate(new LocationCoordinate(BASE_COORD * 2, BASE_COORD / 5)).category(NewsCategory.SHOW).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(6).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD * 2)).category(NewsCategory.SPORT).build()
    );

    private final NewsFactDetail.Builder NEWS_FACT_DETAIL_BUILDER = new NewsFactDetail.Builder()
        .id(-1)
        .geoCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD))
        .date("2020-01-10")
        .time("10h46m")
        .category(NewsCategory.CULTURE)
        .country("France")
        .city("Paris")
        .address("Place de la République, 75011 Paris, France")
        .videoPath("/content/video/small.mp4");

    public List<NewsFactNoDetail> getAll() {
        return new ArrayList<>(ALL_NEWS_FACTS_NO_DETAIL);
    }

    public List<NewsFactNoDetail> filterByCategories(List<NewsCategory> categories) {
        return ALL_NEWS_FACTS_NO_DETAIL
            .stream()
            .filter(newsFactNoDetail -> categories.contains(newsFactNoDetail.getCategory()))
            .collect(Collectors.toList());
    }

    public NewsFactDetail getById(long id) {
        return NEWS_FACT_DETAIL_BUILDER.id(id).build();
    }
}
