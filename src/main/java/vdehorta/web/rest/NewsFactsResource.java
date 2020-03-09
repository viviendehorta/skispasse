package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.bean.NewsFactDetail;
import vdehorta.bean.NewsFactNoDetail;
import vdehorta.repository.NewsFactRepository;

import java.util.Collection;

@RestController
@RequestMapping("/newsFacts")
public class NewsFactsResource {

    private NewsFactRepository newsFactRepository = new NewsFactRepository();

    /**
     * {@code POST  /all} : get the blob containing all news facts data.
     *
     * @return the NewsFactsBlob containing all news facts data.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<NewsFactNoDetail> fetchAllNewsFacts() {
        return newsFactRepository.getAll();
    }

    /**
     * {@code POST  /all} : get the blob containing all news facts data.
     *
     * @return the NewsFactsBlob containing all news facts data.
     */
    @PostMapping(value = "/{newsFactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsFactDetail getNewsFactDetail(@PathVariable long newsFactId) {
        return newsFactRepository.getById(newsFactId);
    }
}
