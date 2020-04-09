package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import vdehorta.domain.Authority;
import vdehorta.domain.User;
import vdehorta.security.AuthoritiesConstants;

import java.time.Instant;

/**
 * Migration: 09/04/2020
 */
@ChangeLog(order = "20200409")
public class Migration20200409 {

    @ChangeSet(order = "01", author = "admin", id = "03-removeUserUser")
    public void removeUserUser(MongoTemplate mongoTemplate) {
        User userUser = new User();
        userUser.setId("user-3");
        mongoTemplate.remove(userUser);
    }

    @ChangeSet(order = "02", author = "admin", id = "02-addContributorAuthority")
    public void addContributorAuthority(MongoTemplate mongoTemplate) {
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(AuthoritiesConstants.CONTRIBUTOR);
        mongoTemplate.save(contributorAuthority);
    }

    @ChangeSet(order = "03", author = "admin", id = "03-addContributorUser")
    public void addContributorUser(MongoTemplate mongoTemplate) {
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(AuthoritiesConstants.CONTRIBUTOR);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        User contributorUser = new User();
        contributorUser.setId("user-3");
        contributorUser.setLogin("contributor");
        contributorUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        contributorUser.setFirstName("");
        contributorUser.setLastName("Contributor");
        contributorUser.setEmail("contributor@localhost");
        contributorUser.setActivated(true);
        contributorUser.setLangKey("fr");
        contributorUser.setCreatedBy("system");
        contributorUser.setCreatedDate(Instant.now());
        contributorUser.getAuthorities().add(contributorAuthority);
        contributorUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(contributorUser);
    }
}
