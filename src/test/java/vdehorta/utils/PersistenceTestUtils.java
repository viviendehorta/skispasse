package vdehorta.utils;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

/**
 * Utility class for testing REST controllers.
 */
public final class PersistenceTestUtils {

    /**
     * Reset all collections from the given MongoTemplate.
     * Used from integration test to clean test database before each test
     * @param mongoTemplate mongo template containing the collections to reset
     */
    public static void resetDatabase(MongoTemplate mongoTemplate) {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        collectionNames.forEach(collectionName -> mongoTemplate.getCollection(collectionName).drop());
    }

    private PersistenceTestUtils() {}
}
