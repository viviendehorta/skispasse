package vdehorta.utils;

import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import vdehorta.config.ApplicationProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for testing REST controllers.
 */
public final class PersistenceTestUtils {

    /**
     * Reset collections for entities managed by the application, creating collections if not existing and emptying all
     * existing documents.
     * Used from integration test to clean test database before each test
     * @param mongoTemplate Mongo template containing the collections to reset
     * @param applicationProperties Application properties
     */
    public static void resetDatabase(MongoTemplate mongoTemplate, ApplicationProperties applicationProperties) {

        //Create empty collections for all managed entities
        List<String> managedCollectionNames = mongoTemplate.getConverter().getMappingContext().getPersistentEntities().stream()
                .map(MongoPersistentEntity::getCollection)
                .collect(Collectors.toList());
        String newsFactVideoBucket = applicationProperties.getMongo().getGridFs().getNewsFactVideoBucket();
        managedCollectionNames.add(newsFactVideoBucket + ".chunks");
        managedCollectionNames.add(newsFactVideoBucket + ".files");
        managedCollectionNames.forEach(collectionName -> {
            if (!mongoTemplate.collectionExists(collectionName)) {
                mongoTemplate.createCollection(collectionName);
            }
            mongoTemplate.getCollection(collectionName).deleteMany(new BasicDBObject());
        });
    }

    private PersistenceTestUtils() {}
}
