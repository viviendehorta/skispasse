package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.service.NewsFactService;

import java.util.List;

@RestController
@RequestMapping("/newsFact")
public class NewsFactResource {

    private NewsFactService newsFactService;

    public NewsFactResource(NewsFactService newsFactService) {
        this.newsFactService = newsFactService;
    }

    /**
     * {@code POST  /all} : Get a list containing all news facts.
     *
     * @return list map containing news facts by news category id.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NewsFactNoDetailDto> getAll() {
        return newsFactService.getAll();
    }

    /**
     * {@code POST  /{newsFactId}} : Get the news fact detail having the given news fact id.
     *
     * @return news fact detail having the given news fact id.
     */
    @PostMapping(value = "/{newsFactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsFactDetailDto getById(@PathVariable long newsFactId) {
        return newsFactService.getById(newsFactId);
    }
}
