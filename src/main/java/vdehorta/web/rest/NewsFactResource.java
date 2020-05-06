package vdehorta.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.service.NewsFactService;
import vdehorta.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/newsFact")
public class NewsFactResource {

    private final Logger log = LoggerFactory.getLogger(NewsFactResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

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
     * {@code GET /contributor/{login}} : Get a list containing all news facts of the given contributor user.
     *
     * @param login    Login of the contributor owner
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body news facts of the given contributor user.
     */
    @GetMapping(value = "/contributor/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsFactDetailDto>> getByOwner(@PathVariable String login, Pageable pageable) {
        Page<NewsFactDetailDto> ownerNewsFactPage = newsFactService.getByOwner(pageable, login);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), ownerNewsFactPage);
        return new ResponseEntity<>(ownerNewsFactPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code POST  / : Create a news fact.
     *
     * @param newsFact The NewsFactDetail containing all data of news fact to create
     * @return A ResponseEntity with a body containing the created news fact
     * @throws URISyntaxException
     */
    @PostMapping()
    public ResponseEntity<NewsFactDetailDto> createNewsFact(@Valid @RequestBody NewsFactDetailDto newsFact) throws URISyntaxException {
        log.debug("REST request to create a news fact : {}", newsFact);

        if (newsFact.getId() != null) {
            throw new BadRequestAlertException("A news fact to create cannot have an id!", "news-fact", "idExists");
        } else {
            NewsFactDetailDto createdNewsFact = newsFactService.create(newsFact);
            return ResponseEntity
                .created(new URI("/newsFact/" + createdNewsFact.getId()))
                .headers(HeaderUtil.createAlert(applicationName, "myNewsFacts.creation.created", createdNewsFact.getId()))
                .body(createdNewsFact);
        }
    }

    /**
     * {@code DELETE /newssFact/:newsFactId} : delete the news fact with id {newsFactId}.
     *
     * @param newsFactId the news fact id to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{newsFactId}")
    public ResponseEntity<Void> deleteNewsFact(@PathVariable String newsFactId) {
        log.debug("REST request to delete a news fact: {}", newsFactId);
        newsFactService.delete(newsFactId);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "myNewsFacts.delete.deleted", newsFactId)).build();
    }

    /**
     * {@code PUT  / : Update a news fact.
     *
     * @param newsFact The NewsFactDetail containing all data of news fact to update
     * @return A ResponseEntity with a body containing the updated news fact
     * @throws URISyntaxException
     */
    @PutMapping
    public ResponseEntity<NewsFactDetailDto> updateNewsFact(@Valid @RequestBody NewsFactDetailDto newsFact) {
        log.debug("REST request to update a news fact : {}", newsFact);

        if (newsFact.getId() == null) {
            throw new BadRequestAlertException("A news fact to update must have an id!", "news-fact", "idNull");
        }

        return ResponseUtil.wrapOrNotFound(
            newsFactService.update(newsFact),
            HeaderUtil.createAlert(applicationName, "myNewsFacts.edition.updated", newsFact.getId()));
    }
}
