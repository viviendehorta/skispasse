package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.bean.NewsCategory;
import vdehorta.repository.NewsCategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/newsCategory")
public class NewsCategoryResource {

    private NewsCategoryRepository newsCategoryRepository = new NewsCategoryRepository();

    /**
     * {@code POST  /all} : Get a list containing all news categories.
     *
     * @return list containing all news categories.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NewsCategory> getAll() {
        return newsCategoryRepository.getAll();
    }
}
