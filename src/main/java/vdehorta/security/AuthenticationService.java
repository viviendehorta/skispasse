package vdehorta.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vdehorta.service.AuthenticationRequiredException;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility Service for Spring Security.
 *
 * Should be used by all other services instead of calling SecurityContextHolder static methods.
 * This allows mocking of security methods in unit tests.
 */
@Service
public class AuthenticationService {

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                }
                return null;
            });
    }

    /**
     * Get the login of the current user or throw error if there is nobody connected.
     * @return the login of the current user
     * @throws AuthenticationRequiredException if there is nobody connected
     */
    public String getCurrentUserLoginOrThrowError() throws AuthenticationRequiredException {
        return getCurrentUserLogin().orElseThrow(AuthenticationRequiredException::new);
    }

    /**
     * If the current user has a specific role (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param role the role to check.
     * @return true if the current user has the role, false otherwise.
     */
    protected boolean isCurrentUserInRole(RoleEnum role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).anyMatch(role.getValue()::equals);
    }

    public void assertIsAuthenticatedRole(RoleEnum requiredRole) throws AuthenticationRequiredException {
        if (!isCurrentUserInRole(requiredRole)) {
            throw new AuthenticationRequiredException(requiredRole);
        }
    }

    private Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority);
    }
}
