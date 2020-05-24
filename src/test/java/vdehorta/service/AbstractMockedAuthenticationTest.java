package vdehorta.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import vdehorta.security.AuthenticationService;
import vdehorta.security.RoleEnum;

import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;

public abstract class AbstractMockedAuthenticationTest {

    protected AuthenticationService authenticationServiceMock;

    @BeforeEach
    public void setup() {
        authenticationServiceMock = Mockito.mock(AuthenticationService.class);
    }

    protected void mockAuthenticatedRole(RoleEnum authenticatedRole) {
        Stream.of(RoleEnum.values())
                .filter(roleEnum -> !roleEnum.equals(authenticatedRole))
                .forEach(roleEnum -> {
                    doThrow(AuthenticationRequiredException.class).when(authenticationServiceMock).assertIsAuthenticatedRole(roleEnum);
                });
    }
}
