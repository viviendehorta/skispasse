package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vdehorta.domain.Authority;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
