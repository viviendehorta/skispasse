package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vdehorta.domain.NewsCategory;
import vdehorta.domain.NewsFact;
import vdehorta.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the {@link NewsCategory} entity.
 */
@Repository
public interface NewsCategoryRepository extends MongoRepository<NewsCategory, Integer> {

//    protected final List<NewsCategory> NEWS_CATEGORIES = Arrays.asList(
//        NEWS_CATEGORY_BUILDER.id(1).label("Manifestation").build(),
//        NEWS_CATEGORY_BUILDER.id(2).label("Sport").build(),
//        NEWS_CATEGORY_BUILDER.id(3).label("Culture").build(),
//        NEWS_CATEGORY_BUILDER.id(4).label("Spectacle").build(),
//        NEWS_CATEGORY_BUILDER.id(5).label("Nature").build(),
//        NEWS_CATEGORY_BUILDER.id(6).label("Autre").build()
//    );

    List<NewsCategory> findAll();
}
