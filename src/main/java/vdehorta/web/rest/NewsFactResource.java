package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.bean.NewsFactDetail;
import vdehorta.bean.NewsFactNoDetail;
import vdehorta.repository.NewsFactRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/newsFacts")
public class NewsFactResource {

    private NewsFactRepository newsFactRepository = new NewsFactRepository();

    /**
     * {@code POST  /all} : Get a list containing all news facts.
     *
     * @return list map containing news facts by news category id.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NewsFactNoDetail> getAll() {
        return newsFactRepository.getAll();
    }

    /**
     * {@code POST  /{newsFactId}} : Get the news fact detail having the given news fact id.
     *
     * @return news fact detail having the given news fact id.
     */
    @PostMapping(value = "/{newsFactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsFactDetail getById(@PathVariable long newsFactId) {
        return newsFactRepository.getById(newsFactId);
    }
}
