package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vdehorta.domain.NewsFact;

import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link NewsFact} entity.
 */
@Repository
public interface NewsFactRepository extends MongoRepository<NewsFact, String> {

    List<NewsFact> findAll();

    List<NewsFact> findAllByCreatedBy(String createdBy);
}
