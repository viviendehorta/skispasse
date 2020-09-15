package vdehorta.config;

import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile(ProfileConstants.SPRING_PROFILE_DEV)
public class MongockConfiguration {

    private final Logger log = LoggerFactory.getLogger(MongockConfiguration.class);

    @Bean
    public SpringBootMongock mongock(MongoTemplate mongoTemplate, ApplicationContext springContext) {
        log.debug("Configuring Mongock");
        return new SpringBootMongockBuilder(mongoTemplate, "vdehorta.config.dbmigrations")
                .setApplicationContext(springContext)
                .setLockQuickConfig()
                .setEnabled(true)
                .build();
    }
}
