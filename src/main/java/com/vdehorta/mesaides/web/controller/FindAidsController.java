package com.vdehorta.mesaides.web.controller;

import com.vdehorta.mesaides.model.dto.FindAidsResultDto;
import com.vdehorta.mesaides.model.dto.PersonalDetailDto;
import com.vdehorta.mesaides.service.FindAidsService;
import com.vdehorta.mesaides.web.validator.PersonalDetailValidator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class FindAidsController {

    private static final Logger logger = LoggerFactory.getLogger(FindAidsController.class);

    private final FindAidsService findAidsService;

    @InitBinder(value = "infoDto")
    protected void initInfoDtoBinding(WebDataBinder binder) {
        binder.setValidator(new PersonalDetailValidator());
    }

    public FindAidsController(FindAidsService findAidsService) {
        this.findAidsService = findAidsService;
    }

    @PostMapping(value = "/find-aids", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<FindAidsResultDto> findAids(@RequestBody @Valid PersonalDetailDto personalDetail) {
        logger.info("Find aids: {}", personalDetail);
        return ResponseEntity.ok(findAidsService.findAids(personalDetail));
    }
}
