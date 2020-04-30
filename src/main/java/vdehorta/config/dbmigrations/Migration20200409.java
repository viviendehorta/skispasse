package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.google.common.collect.Sets;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import vdehorta.domain.Authority;
import vdehorta.domain.User;
import vdehorta.security.AuthoritiesConstants;
import vdehorta.service.ClockService;

/**
 * Migration: 09/04/2020
 */
@ChangeLog(order = "20200409")
public class Migration20200409 {

    private ClockService clockService = new ClockService();

    @ChangeSet(order = "01", author = "admin", id = "01-addContributorAuthority")
    public void addContributorAuthority(MongoTemplate mongoTemplate) {
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(AuthoritiesConstants.CONTRIBUTOR);
        mongoTemplate.save(contributorAuthority);
    }

    @ChangeSet(order = "02", author = "admin", id = "02-addContributorAuthorityToAdminAndSystemUsers")
    public void addContributorAuthorityToAdminAndSystemUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(AuthoritiesConstants.CONTRIBUTOR);

        // Update system user
        Query systemQuery = new Query();
        systemQuery.addCriteria(Criteria.where("login").is("system"));
        Update systemUpdate = new Update();
        systemUpdate.set("authorities", Sets.newHashSet(userAuthority, adminAuthority, contributorAuthority));
        mongoTemplate.updateFirst(systemQuery, systemUpdate, User.class);

        // Update admin user
        Query adminQuery = new Query();
        adminQuery.addCriteria(Criteria.where("login").is("admin"));
        Update adminUpdate = new Update();
        adminUpdate.set("authorities", Sets.newHashSet(userAuthority, adminAuthority, contributorAuthority));
        mongoTemplate.updateFirst(adminQuery, adminUpdate, User.class);
    }

    @ChangeSet(order = "03", author = "admin", id = "03-addContributorUser")
    public void addContributorUser(MongoTemplate mongoTemplate) {
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(AuthoritiesConstants.CONTRIBUTOR);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        User contributorUser = new User();
        contributorUser.setLogin("contributor");
        contributorUser.setPassword("$2a$10$CAwQvWjl.qsMknf1STzDZ.rm6iCkXnN3YfgiJcvdyFjdtfjL3wV6S");
        contributorUser.setFirstName("");
        contributorUser.setLastName("Contributor");
        contributorUser.setEmail("contributor@localhost");
        contributorUser.setActivated(true);
        contributorUser.setLangKey("fr");
        contributorUser.setCreatedBy("system");
        contributorUser.setCreatedDate(clockService.now());
        contributorUser.getAuthorities().add(contributorAuthority);
        contributorUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(contributorUser);
    }
}
