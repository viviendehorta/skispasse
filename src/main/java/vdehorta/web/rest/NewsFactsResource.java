package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vdehorta.bean.LocationCoordinate;
import vdehorta.bean.NewsFact;
import vdehorta.bean.NewsFactsBlob;

import java.util.Arrays;

@RestController
@RequestMapping("/newsFacts")
public class NewsFactsResource {

    /**
     * {@code POST  /all} : get the blob containing all news facts data.
     *
     * @return the NewsFactsBlob containing all news facts data.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsFactsBlob getNewsFactsBlob() {
        final long e = 4500000L;
        return new NewsFactsBlob(Arrays.asList(new NewsFact(new LocationCoordinate(e, e))));
    }
}
