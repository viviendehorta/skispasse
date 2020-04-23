package vdehorta.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
     * {@code GET  /contributor/{login}} : Get a list containing all news facts of the given contributor user.
     * @param login Login of the contributor owner
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body news facts of the given contributor user.
     */
    @GetMapping(value = "/contributor/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsFactDetailDto>> getByOwner(@PathVariable String login, Pageable pageable) {
        Page<NewsFactDetailDto> ownerNewsFactPage = newsFactService.getByOwner(pageable, login);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), ownerNewsFactPage);
        return new ResponseEntity<>(ownerNewsFactPage.getContent(), headers, HttpStatus.OK);
    }
}
