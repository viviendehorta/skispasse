package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vdehorta.domain.NewsCategory;

import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link NewsCategory} entity.
 */
@Repository
public interface NewsCategoryRepository extends MongoRepository<NewsCategory, String> {
}
