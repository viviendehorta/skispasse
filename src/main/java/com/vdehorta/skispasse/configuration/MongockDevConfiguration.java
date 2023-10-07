package com.vdehorta.skispasse.configuration;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.RunnerSpringbootBuilder;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
@Profile({"dev"})
public class MongockDevConfiguration {

    /**
     * Set driver bean in context, for testing purpose only
     * Allow tests to get and check bean state
     */
    @Bean
    public SpringDataMongoV4Driver mongockDriver(MongoTemplate mongoTemplate) {
        // Driver
        SpringDataMongoV4Driver driver = SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
        driver.setWriteConcern(WriteConcern.MAJORITY.withJournal(true).withWTimeout(1000, TimeUnit.MILLISECONDS));
        driver.setReadConcern(ReadConcern.MAJORITY);
        driver.setReadPreference(ReadPreference.primary());
        driver.enableTransaction();
        return driver;
    }

    /**
     * Set driver bean in context for testing purpose only
     * Allow tests to get and check bean state
     */
    @Bean
    public RunnerSpringbootBuilder mongockRunnerBuilder(ApplicationContext springContext,
                                                        SpringDataMongoV4Driver mongockDriver) {
        return MongockSpringboot.builder()
                .setDriver(mongockDriver)
                .addMigrationScanPackage("com.vdehorta.skispasse.dbmigration.dev")
                .setEventPublisher(springContext)
                .setSpringContext(springContext)
                .setTransactionEnabled(true);
    }

    @Bean
    public MongockApplicationRunner mongockApplicationRunner(RunnerSpringbootBuilder mongockRunnerBuilder) {
        return mongockRunnerBuilder.buildApplicationRunner();
    }
}
