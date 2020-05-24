package vdehorta.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the {@link AuthenticationService} utility class.
 */
public class SecurityServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationService();
    }

    @Test
    public void testGetCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);
        Optional<String> login = authenticationService.getCurrentUserLogin();
        assertThat(login).contains("admin");
    }

    @Test
    public void testIsCurrentUserInRole() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleEnum.USER.getValue()));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
        SecurityContextHolder.setContext(securityContext);

        assertThat(authenticationService.isCurrentUserInRole(RoleEnum.USER)).isTrue();
        assertThat(authenticationService.isCurrentUserInRole(RoleEnum.ADMIN)).isFalse();
    }

}
