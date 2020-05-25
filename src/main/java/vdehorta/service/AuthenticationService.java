package vdehorta.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vdehorta.security.RoleEnum;
import vdehorta.service.errors.AuthenticationRequiredException;
import vdehorta.service.errors.ForbiddenActionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility Service for Spring Security.
 * <p>
 * Should be used by all other services instead of calling SecurityContextHolder static methods.
 * This allows mocking of security methods in unit tests.
 */
@Service
public class AuthenticationService {

    /**
     * Get the login of the current user or null if nobody is connected.
     * Should always be called after asserting user is authenticated to avoid unexpected null value
     *
     * @return the login of the current user, null  if nobody is connected
     */
    public String getCurrentUserLoginOrNull() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getLogin(authentication);
    }

    /**
     * Get the login of the current user in an Optional object.
     * @return an Optional containing the login of the current user or empty if nobody is connected
     */
    public Optional<String> getCurrentUserLoginOptional() {
        return getAuthenticationOptional().map(this::getLogin);
    }

    public void assertCurrentUserHasRole(RoleEnum requiredRole) throws AuthenticationRequiredException, ForbiddenActionException {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new AuthenticationRequiredException();
        }
        if (!getRoles(authentication).contains(requiredRole.getValue())) {
            throw new ForbiddenActionException(requiredRole);
        }
    }

    /**
     * Get the SpringSecurity authentication
     * @return The SpringSecurity authentication or null if user is not authenticated
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Get the SpringSecurity authentication in an Optional
     * @return TAn Optional containing he SpringSecurity authentication or empty if user is not authenticated
     */
    private Optional<Authentication> getAuthenticationOptional() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Extract the login from the given authentication object
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

    /**
     * Get a stream containing all the roles (authorities for SpringSecurity) contained in the given Authentication object
     * @param authentication
     * @return a stream containing all the roles (authorities for SpringSecurity) contained in the given Authentication object
     */
    private List<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
