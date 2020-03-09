package vdehorta.repository;

import org.junit.jupiter.api.Test;
import vdehorta.bean.NewsFactDetail;
import vdehorta.bean.NewsFactNoDetail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static vdehorta.bean.NewsCategory.*;

public class NewsFactRepositoryTest {

    private NewsFactRepository newsFactRepository = new NewsFactRepository();

    @Test
    void getAll_shouldReturnAIdenticalSizedListThanRepository() {
        List<NewsFactNoDetail> allNewsFacts = newsFactRepository.getAll();
        assertThat(allNewsFacts).hasSameSizeAs(newsFactRepository.ALL_NEWS_FACTS_NO_DETAIL);
    }

    @Test
    void filterByCategories_shouldReturnNewsFactsWithSpecifiedCategory() {
        List<NewsFactNoDetail> cultureCategoryNewsFacts = newsFactRepository.filterByCategories(Collections.singletonList(CULTURE));
        assertThat(cultureCategoryNewsFacts).allMatch(newsFactNoDetail -> newsFactNoDetail.getCategory().equals(CULTURE));
    }

    @Test
    void filterByCategories_shouldReturnNewsFactsWithOneOfTheSpecifiedCategories() {
        List<NewsFactNoDetail> natureAndOtherCategoryNewsFacts = newsFactRepository.filterByCategories(Arrays.asList(NATURE, OTHER));
        assertThat(natureAndOtherCategoryNewsFacts).allMatch(newsFactNoDetail ->
            newsFactNoDetail.getCategory().equals(NATURE)
                || newsFactNoDetail.getCategory().equals(OTHER));
    }

    @Test
    void getById_shouldReturnANewsFactDetailWithSameIdAsSpecified() {
        final long NEWS_FACT_ID = 2;
        NewsFactDetail newsFactDetail = newsFactRepository.getById(NEWS_FACT_ID);
        assertThat(newsFactDetail.getId()).isEqualTo(NEWS_FACT_ID);
    }
}
