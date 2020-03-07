package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.bean.LocationCoordinate;
import vdehorta.bean.NewsCategory;
import vdehorta.bean.NewsFactDetail;
import vdehorta.bean.NewsFactNoDetail;

import java.util.Arrays;
import java.util.Collection;

@RestController
@RequestMapping("/newsFacts")
public class NewsFactsResource {

    private static final long BASE_COORD = 4500000L;

    /**
     * {@code POST  /all} : get the blob containing all news facts data.
     *
     * @return the NewsFactsBlob containing all news facts data.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<NewsFactNoDetail> fetchAllNewsFacts() {
        NewsFactNoDetail.Builder builder = new NewsFactNoDetail.Builder();
        return Arrays.asList(
            builder.id(1).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD)).category(NewsCategory.CULTURE).build(),
            builder.id(2).locationCoordinate(new LocationCoordinate(BASE_COORD / 2, BASE_COORD)).category(NewsCategory.DEMONSTRATION).build(),
            builder.id(3).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD / 3)).category(NewsCategory.NATURE).build(),
            builder.id(4).locationCoordinate(new LocationCoordinate(BASE_COORD / 4, BASE_COORD / 4)).category(NewsCategory.OTHER).build(),
            builder.id(5).locationCoordinate(new LocationCoordinate(BASE_COORD * 2, BASE_COORD / 5)).category(NewsCategory.SHOW).build(),
            builder.id(6).locationCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD * 2)).category(NewsCategory.SPORT).build()
        );
    }

    /**
     * {@code POST  /all} : get the blob containing all news facts data.
     *
     * @return the NewsFactsBlob containing all news facts data.
     */
    @PostMapping(value = "/{newsFactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsFactDetail getNewsFactDetail(@PathVariable long newsFactId) {
        return new NewsFactDetail.Builder()
            .id(newsFactId)
            .geoCoordinate(new LocationCoordinate(BASE_COORD, BASE_COORD))
            .date("2020-01-10")
            .time("10h46m")
            .category(NewsCategory.CULTURE)
            .country("France")
            .city("Paris")
            .address("Place de la République, 75011 Paris, France")
            .videoPath("/content/video/small.mp4")
            .build();
    }
}
