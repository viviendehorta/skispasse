package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import vdehorta.domain.PersistentToken;
import vdehorta.domain.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link PersistentToken} entity.
 */
public interface PersistentTokenRepository extends MongoRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
