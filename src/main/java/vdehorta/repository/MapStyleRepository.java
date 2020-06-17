package vdehorta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vdehorta.domain.MapStyle;

/**
 * Spring Data MongoDB repository for the application map styles.
 */
@Repository
public interface MapStyleRepository extends MongoRepository<MapStyle, String> {
}
