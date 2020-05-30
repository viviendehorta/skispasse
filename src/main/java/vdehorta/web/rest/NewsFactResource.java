package vdehorta.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vdehorta.config.ApplicationProperties;
import vdehorta.converter.JacksonMapperFactory;
import vdehorta.dto.NewsFactDetailDto;
import vdehorta.dto.NewsFactNoDetailDto;
import vdehorta.service.AuthenticationService;
import vdehorta.service.NewsFactService;
import vdehorta.web.rest.errors.BadRequestAlertException;
import vdehorta.web.rest.util.HeaderUtil;
import vdehorta.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static vdehorta.security.RoleEnum.CONTRIBUTOR;

@RestController
@RequestMapping("/newsFacts")
public class NewsFactResource {

    private final Logger log = LoggerFactory.getLogger(NewsFactResource.class);

    private String applicationName;
    private NewsFactService newsFactService;
    private AuthenticationService authenticationService;

    public NewsFactResource(ApplicationProperties applicationProperties, NewsFactService newsFactService, AuthenticationService authenticationService) {
        this.applicationName = applicationProperties.getClientAppName();
        this.newsFactService = newsFactService;
        this.authenticationService = authenticationService;
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
     * {@code GET /contributor} : Get a list containing all news facts of the current user, if contributor.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body news facts of the current  user, if contributor;
     * If user is not authenticated, the {@link ResponseEntity} has status {@code 401 (UNAUTHORIZED)}
     * If user is not contributor, the {@link ResponseEntity} has status {@code 403 (FORBIDDEN)}
     */
    @GetMapping(value = "/contributor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NewsFactDetailDto>> getMyNewsFacts(Pageable pageable) {
        log.debug("REST request to get current user  news facts");

        authenticationService.assertAuthenticationRole(CONTRIBUTOR);

        Page<NewsFactDetailDto> ownerNewsFactPage = newsFactService.getUserNewsFacts(authenticationService.getCurrentUserLoginOrThrowError(), pageable);
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
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<NewsFactDetailDto> createNewsFact(@RequestParam("videoFile") MultipartFile videoFile, @RequestParam("newsFactJson") String newsFactJson) throws URISyntaxException {
        log.debug("REST request to create a news fact : {}", newsFactJson);

        authenticationService.assertAuthenticationRole(CONTRIBUTOR);

        NewsFactDetailDto newsFact;
        try {
            newsFact = JacksonMapperFactory.getObjectMapper().readValue(newsFactJson, NewsFactDetailDto.class);
        } catch (IOException e) {
            throw new BadRequestAlertException("News fact data is invalid!");
        }
        if (newsFact.getId() != null) {
            throw new BadRequestAlertException("A news fact to create can't already have an id!");
        }

        //Reset the input news fact video path because browser inits with a fake value
        newsFact.setVideoPath(null);

        NewsFactDetailDto createdNewsFact = newsFactService.create(newsFact, videoFile, authenticationService.getCurrentUserLoginOrThrowError());
        return ResponseEntity
                .created(new URI("/newsFacts/" + createdNewsFact.getId()))
                .headers(HeaderUtil.createAlertHeaders(applicationName, "News fact with id '" + createdNewsFact.getId() + "' was created."))
                .body(createdNewsFact);
    }

    /**
     * {@code DELETE /newssFact/:newsFactId} : delete the news fact with id {newsFactId}.
     *
     * @param newsFactId the news fact id to delete.videoPath = "C:\fakepath\olmap.txt"
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{newsFactId}")
    public ResponseEntity<Void> deleteNewsFact(@PathVariable String newsFactId) {
        log.debug("REST request to delete the news fact with id: {}", newsFactId);

        authenticationService.assertAuthenticationRole(CONTRIBUTOR);
        newsFactService.delete(newsFactId, authenticationService.getCurrentUserLoginOrThrowError());
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createAlertHeaders(applicationName, "News fact with id '" + newsFactId + "' was deleted."))
                .build();
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

        authenticationService.assertAuthenticationRole(CONTRIBUTOR);

        if (newsFact.getId() == null) {
            throw new BadRequestAlertException("A news fact to update must have an id!");
        }

        NewsFactDetailDto updated = newsFactService.update(newsFact, authenticationService.getCurrentUserLoginOrThrowError());

        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlertHeaders(applicationName, "News fact with id '" + newsFact.getId() + "' was updated."))
                .body(updated);
    }
}
