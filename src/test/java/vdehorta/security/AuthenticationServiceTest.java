package vdehorta.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vdehorta.service.AuthenticationService;
import vdehorta.service.errors.AuthenticationRequiredException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class for the {@link AuthenticationService} utility class.
 */
public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationService();
    }

    @Test
    public void getCurrentUserLoginOrThrowError_shouldReturnTheAuthenticatedUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        String login = authenticationService.getCurrentUserLoginOrThrowError();

        assertThat(login).contains("admin");
    }

    @Test
    public void getCurrentUserLoginOrThrowError_shouldRThrowErrorIfNobodyIsAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(securityContext);
        assertThatThrownBy(() -> authenticationService.getCurrentUserLoginOrThrowError()).isInstanceOf(AuthenticationRequiredException.class);
    }

}
