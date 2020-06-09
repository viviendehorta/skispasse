package vdehorta.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.BsonDocument;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Reset all collections from the given MongoTemplate.
     * Used from integration test to clean test database before each test
     * @param mongoTemplate mongo template containing the collections to reset
     */
    public static void resetDatabase(MongoTemplate mongoTemplate) {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        collectionNames.forEach(collectionName -> mongoTemplate.getCollection(collectionName).deleteMany(BsonDocument.parse("{}")));
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    /**
     * Convert an object to JSON String.
     *
     * @param object the object to convert.
     * @return the JSON String value.
     * @throws IOException
     */
    public static String convertObjectToJsonString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }


    /**
     * Verifies the equals/hashcode contract on the domain object.
     */
    public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        assertThat(domainObject1.toString()).isNotNull();
        assertThat(domainObject1).isEqualTo(domainObject1);
        assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
        // Test with an instance of another class
        Object testOtherObject = new Object();
        assertThat(domainObject1).isNotEqualTo(testOtherObject);
        assertThat(domainObject1).isNotEqualTo(null);
        // Test with an instance of the same class
        T domainObject2 = clazz.getConstructor().newInstance();
        assertThat(domainObject1).isNotEqualTo(domainObject2);
    }


    private TestUtil() {}
}
