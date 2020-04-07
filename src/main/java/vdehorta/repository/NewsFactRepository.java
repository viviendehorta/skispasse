package vdehorta.repository;

import vdehorta.domain.LocationCoordinate;
import vdehorta.domain.NewsFactDetail;
import vdehorta.domain.NewsFactNoDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsFactRepository {

    private static final long BASE_COORD = 4500000L;

    private final NewsFactNoDetail.Builder NEWS_FACT_NO_DETAIL_BUILDER = new NewsFactNoDetail.Builder();

    private final NewsCategoryRepository newsCategoryRepository = new NewsCategoryRepository();

    protected final List<NewsFactNoDetail> ALL_NEWS_FACTS_NO_DETAIL = Arrays.asList(
        NEWS_FACT_NO_DETAIL_BUILDER.id(1).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD)).categoryId(1).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(2).locationCoordinate(new LocationCoordinate(BASE_COORD / 2, BASE_COORD)).categoryId(2).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(3).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD / 3)).categoryId(3).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(4).locationCoordinate(new LocationCoordinate(BASE_COORD / 4, BASE_COORD / 4)).categoryId(4).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(5).locationCoordinate(new LocationCoordinate(BASE_COORD * 2, BASE_COORD / 5)).categoryId(5).build(),
        NEWS_FACT_NO_DETAIL_BUILDER.id(6).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD * 2)).categoryId(6).build()
    );

    private final NewsFactDetail.Builder NEWS_FACT_DETAIL_BUILDER = new NewsFactDetail.Builder()
        .id(-1)
        .geoCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD))
        .date("2020-01-10")
        .time("10h46m")
        .category(newsCategoryRepository.getById(1))
        .country("France")
        .city("Paris")
        .address("Place de la République, 75011 Paris, France")
        .videoPath("/content/videos/small.mp4");

    public List<NewsFactNoDetail> getAll() {
        return new ArrayList<>(ALL_NEWS_FACTS_NO_DETAIL);
    }

    public NewsFactDetail getById(long id) {
        return NEWS_FACT_DETAIL_BUILDER.id(id).build();
    }
}
