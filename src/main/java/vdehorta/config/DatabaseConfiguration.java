package vdehorta.config;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories("vdehorta.repository")
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(
    auditorAwareRef = "springSecurityAuditorAware",
    setDates = false
)
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final ApplicationProperties applicationProperties;
    private MongoDbFactory mongoDbFactory;
    private MongoConverter mongoConverter;
    private Environment environment;

    public DatabaseConfiguration(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter, ApplicationProperties applicationProperties, Environment environment) {
        this.mongoDbFactory = mongoDbFactory;
        this.mongoConverter = mongoConverter;
        this.applicationProperties = applicationProperties;
        this.environment = environment;
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
        log.debug("Configuring Mongobee");
        Mongobee mongobee = new Mongobee(mongoClient);
        mongobee.setDbName(mongoProperties.getMongoClientDatabase());
        mongobee.setMongoTemplate(mongoTemplate);
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage("vdehorta.config.dbmigrations");
        mongobee.setEnabled(true);
        mongobee.setSpringEnvironment(environment);
        return mongobee;
    }

    @Bean
    public GridFsTemplate videoGridFsTemplate() {
        GridFsTemplate gridFsTemplate = new GridFsTemplate(
                mongoDbFactory,
                mongoConverter,
                applicationProperties.getMongo().getGridFs().getNewsFactVideoBucket());
        return gridFsTemplate;
    }

//    /**
//     * Allow use of transactions with mongo
//     */
//    @Bean
//    public MongoTransactionManager transactionManager() {
//        return new MongoTransactionManager(mongoDbFactory);
//    }
//
//    /**
//     * Allow use of transactions with mongo
//     */
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        MongoTemplate mongoTemplate = new MongoTemplate(this.mongoDbFactory);
//        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
//        return mongoTemplate;
//    }
}
