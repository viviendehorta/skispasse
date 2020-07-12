package vdehorta.config;

import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories("vdehorta.repository")
@Import(value = {MongoAutoConfiguration.class})
@EnableMongoAuditing(
        auditorAwareRef = "springSecurityAuditorAware",
        setDates = false
)
public class DatabaseConfiguration {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public GridFsTemplate videoGridFsTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter, ApplicationProperties applicationProperties) {
        return new GridFsTemplate(
                mongoDbFactory,
                mongoConverter,
                applicationProperties.getMongo().getGridFs().getNewsFactVideoBucket());
    }

    /**
     * Allow use of transactions with mongo
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory mongoDbFactory) {
        return new MongoTransactionManager(mongoDbFactory);
    }
}
