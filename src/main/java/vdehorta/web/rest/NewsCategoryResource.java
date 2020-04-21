package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.dto.NewsCategoryDto;
import vdehorta.service.NewsCategoryService;

import java.util.List;

@RestController
@RequestMapping("/newsCategory")
public class NewsCategoryResource {

    private NewsCategoryService newsCategoryService;

    public NewsCategoryResource(NewsCategoryService newsCategoryService) {
        this.newsCategoryService = newsCategoryService;
    }

    /**
     * {@code POST  /all} : Get a list containing all news categories.
     *
     * @return list containing all news categories.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NewsCategoryDto> getAll() {
        return newsCategoryService.getAll();
    }
}
