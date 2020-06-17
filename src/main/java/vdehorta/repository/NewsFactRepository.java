package vdehorta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vdehorta.domain.NewsFact;

/**
 * Spring Data MongoDB repository for the {@link NewsFact} entity.
 */
@Repository
public interface NewsFactRepository extends MongoRepository<NewsFact, String> {

    Page<NewsFact> findAllByOwner(Pageable pageable, String owner);
}
