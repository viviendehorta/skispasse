package com.vdehorta.skispasse.web.controller;

import com.vdehorta.skispasse.model.dto.NewsFactDto;
import com.vdehorta.skispasse.service.NewsFactService;
import com.vdehorta.skispasse.web.validator.NewsFactDtoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/newsfact")
public class NewsFactController {

    private static final Logger logger = LoggerFactory.getLogger(NewsFactController.class);

    private final NewsFactService newsFactService;

    @InitBinder(value = "newsFactDto")
    protected void initInfoDtoBinding(WebDataBinder binder) {
        binder.setValidator(new NewsFactDtoValidator());
    }

    public NewsFactController(NewsFactService newsFactService) {
        this.newsFactService = newsFactService;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<List<NewsFactDto>> list() {
        logger.info("List newsfacts");
        return ResponseEntity.ok(newsFactService.list());
    }
}
