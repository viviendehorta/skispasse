package vdehorta.config.dbmigrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.google.common.collect.Sets;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import vdehorta.config.Constants;
import vdehorta.domain.Authority;
import vdehorta.domain.User;
import vdehorta.security.RoleEnum;
import vdehorta.service.ClockService;

@ChangeLog(order = "20200409")
public class Migration20200409_AddContributor {

    @ChangeSet(order = "01", author = "admin", id = "01-addContributorAuthority")
    public void addContributorAuthority(MongoTemplate mongoTemplate) {
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(RoleEnum.CONTRIBUTOR.getValue());
        mongoTemplate.save(contributorAuthority);
    }

    @ChangeSet(order = "02", author = "admin", id = "02-addContributorAuthorityToAdminAndSystemUsers")
    public void addContributorAuthorityToAdminAndSystemUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(RoleEnum.ADMIN.getValue());
        Authority userAuthority = new Authority();
        userAuthority.setName(RoleEnum.USER.getValue());
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(RoleEnum.CONTRIBUTOR.getValue());

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
    public void addContributorUser(MongoTemplate mongoTemplate, ClockService clockService) {
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(RoleEnum.CONTRIBUTOR.getValue());
        Authority userAuthority = new Authority();
        userAuthority.setName(RoleEnum.USER.getValue());

        User contributorUser = new User();
        contributorUser.setLogin("contributor");
        contributorUser.setPassword("$2a$10$CAwQvWjl.qsMknf1STzDZ.rm6iCkXnN3YfgiJcvdyFjdtfjL3wV6S");
        contributorUser.setFirstName("");
        contributorUser.setLastName("Contributor");
        contributorUser.setEmail("contributor@localhost");
        contributorUser.setActivated(true);
        contributorUser.setCreatedBy("system");
        contributorUser.setCreatedDate(clockService.now());
        contributorUser.getAuthorities().add(contributorAuthority);
        contributorUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(contributorUser);
    }
}
