package vdehorta.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vdehorta.service.AuthenticationService;
import vdehorta.service.errors.AuthenticationRequiredException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration test class for the {@link AuthenticationService}.
 */
@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationService();
    }

    @Test
    @WithMockUser(username = "aladdin", roles = {"CONTRIBUTOR"})
    public void getCurrentUserLoginOrThrowError_shouldReturnTheAuthenticatedUserLogin() {
        assertThat(authenticationService.getCurrentUserLoginOrThrowError()).isEqualTo("aladdin");
    }

    @Test
    public void getCurrentUserLoginOrThrowError_shouldThrowErrorIfSpringReturnNullAuthenticated() {
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(); //Check Spring returns null authentication
        assertThatThrownBy(() -> authenticationService.getCurrentUserLoginOrThrowError()).isInstanceOf(AuthenticationRequiredException.class);
    }

    @Test
    @WithMockUser(username = "anonymousUser", roles = {"ANONYMOUS"})
    public void getCurrentUserLoginOrThrowError_shouldThrowErrorIfSpringReturnAnonymousAuthenticated() {
        assertThatThrownBy(() -> authenticationService.getCurrentUserLoginOrThrowError()).isInstanceOf(AuthenticationRequiredException.class);
    }
}
