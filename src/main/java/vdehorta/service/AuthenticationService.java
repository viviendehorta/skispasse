package vdehorta.service;

import com.google.common.base.Preconditions;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vdehorta.security.RoleEnum;
import vdehorta.service.errors.AuthenticationRequiredException;
import vdehorta.service.errors.RoleIsRequiredException;

import java.util.Optional;

/**
 * Utility Service for Spring Security.
 * <p>
 * Should be used by all other services instead of calling SecurityContextHolder static methods.
 * This allows mocking of security methods in unit tests.
 */
@Service
public class AuthenticationService {

    /**
     * Get the login of the authenticated user.
     *
     * @return the login of the authenticated user
     * @throws AuthenticationRequiredException If nobody is authenticated
     */
    public String getCurrentUserLoginOrThrowError() throws AuthenticationRequiredException {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new AuthenticationRequiredException();
        }
        return getLogin(authentication);
    }

    /**
     * Get the login of the current user in an Optional object.
     *
     * @return an Optional containing the login of the current user or empty if nobody is connected
     */
    public Optional<String> getCurrentUserLoginOptional() {
        return getAuthenticationOptional().map(this::getLogin);
    }

    private boolean isAnonymousAuthentication(Authentication authentication) {
        Preconditions.checkNotNull(authentication, "Authentication should not be null!");
        return authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"));
    }

    /**
     * /**
     * Return the spring authentication or null in case of spring "anonymous" token used when nobody is authenticated
     *
     * @return The SpringSecurity authentication or null if user is not authenticated
     */
    private Authentication getAuthentication() {
        Authentication springAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (springAuthentication == null || isAnonymousAuthentication(springAuthentication)) {
            return null;
        }
        return springAuthentication;
    }

    /**
     * Get the SpringSecurity authentication in an Optional
     *
     * @return An Optional containing the SpringSecurity authentication or empty if user is not authenticated
     */
    private Optional<Authentication> getAuthenticationOptional() {
        return Optional.ofNullable(getAuthentication());
    }

    /**
     * Extract the login from the given authentication object
     *
     * @param authentication
     * @return the login contained in the given authentication object
     */
    private String getLogin(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    public void assertAuthenticationRole(RoleEnum requiredRole) throws AuthenticationRequiredException, RoleIsRequiredException {
        Authentication authentication = getAuthenticationOptional().orElseThrow(AuthenticationRequiredException::new);
        if (authentication.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals(requiredRole.getValue()))) {
            throw new RoleIsRequiredException(requiredRole);
        }
    }
}
