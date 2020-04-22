package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
     * {@code GET  /all} : Get a list containing all news facts.
     *
     * @return list containing all news facts.
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NewsFactNoDetailDto> getAll() {
        return newsFactService.getAll();
    }

    /**
     * {@code GET  /{newsFactId}} : Get the news fact detail having the given news fact id.
     *
     * @return news fact detail having the given news fact id.
     */
    @GetMapping(value = "/{newsFactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsFactDetailDto getById(@PathVariable String newsFactId) {
        return newsFactService.getById(newsFactId);
    }

    /**
     * {@code GET  /user} : Get a list containing news facts of the given user.
     *
     * @return list containing news facts of the given user.
     */
    @GetMapping(value = "/contributor/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NewsFactDetailDto> getByUser(@PathVariable String login) {
        return newsFactService.getByOwner(login);
    }
}
