package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vdehorta.domain.Authority;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
@Repository
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
