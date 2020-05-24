package vdehorta.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import vdehorta.config.Constants;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private AuthenticationService securityService;

    public SpringSecurityAuditorAware(AuthenticationService securityService) {
        this.securityService = securityService;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(securityService.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
    }
}
