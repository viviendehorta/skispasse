package vdehorta.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import vdehorta.domain.Authority;
import vdehorta.domain.User;
import vdehorta.security.RoleEnum;
import vdehorta.service.ClockService;

@ChangeLog(order = "20200407")
public class Migration20200407_InitAuthoritiesAndUsers {

    private ClockService clockService = new ClockService();

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {

        Authority adminAuthority = new Authority();
        adminAuthority.setName(RoleEnum.ADMIN.getValue());
        Authority userAuthority = new Authority();
        userAuthority.setName(RoleEnum.USER.getValue());
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
    }

    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(RoleEnum.ADMIN.getValue());
        Authority userAuthority = new Authority();
        userAuthority.setName(RoleEnum.USER.getValue());

        User systemUser = new User();
        systemUser.setLogin("system");
        systemUser.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
        systemUser.setFirstName("");
        systemUser.setLastName("System");
        systemUser.setEmail("system@localhost");
        systemUser.setActivated(true);
        systemUser.setLangKey("fr");
        systemUser.setCreatedBy(systemUser.getLogin());
        systemUser.setCreatedDate(clockService.now());
        systemUser.getAuthorities().add(adminAuthority);
        systemUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(systemUser);

        User adminUser = new User();
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(systemUser.getLogin());
        adminUser.setCreatedDate(clockService.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);
    }
}
