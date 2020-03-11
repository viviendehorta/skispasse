package vdehorta.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vdehorta.bean.NewsCategory;
import vdehorta.bean.NewsFactDetail;
import vdehorta.bean.NewsFactNoDetail;
import vdehorta.repository.NewsFactRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/newsFacts")
public class NewsFactsResource {

    private NewsFactRepository newsFactRepository = new NewsFactRepository();

    /**
     * {@code POST  /all} : Get a list containing all news facts.
     *
     * @return list containing all news facts.
     */
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<NewsFactNoDetail> getAll() {
        return newsFactRepository.getAll();
    }

    /**
     * {@code POST  /filter/categories/{newsCategoryIds}} : Get a list containing the news facts with a category
     * included in the given category id list.
     *
     * @return list containing the news facts with a category included in the given category id list.
     */
    @PostMapping(value = "/filter/categories/{newsCategoryIds}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<NewsFactNoDetail> filterByCategories(@PathVariable List<Integer> newsCategoryIds) {
        List<NewsCategory> newsCategories = newsCategoryIds.stream()
            .map(newsCategoryId -> NewsCategory.fromId(newsCategoryId))
            .collect(Collectors.toList());
        return newsFactRepository.filterByCategories(newsCategories);
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
