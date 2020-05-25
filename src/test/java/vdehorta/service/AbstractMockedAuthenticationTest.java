package vdehorta.service;

import org.mockito.Mockito;
import vdehorta.security.RoleEnum;
import vdehorta.service.errors.AuthenticationRequiredException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public abstract class AbstractMockedAuthenticationTest {

    protected static final String DEFAULT_AUTHENTICATED_USER_LOGIN = "Skisp'";

    protected AuthenticationService authenticationServiceMock;

    public void setup() {
        authenticationServiceMock = Mockito.mock(AuthenticationService.class);
    }

    protected void mockAuthenticated(List<RoleEnum> roles) {
        mockAuthenticated(DEFAULT_AUTHENTICATED_USER_LOGIN, roles);
    }

    /**
     * Mock all public methods of AuthenticationService mock to act like with an authenticated  user matching given login and roles
     *
     * @param login
     * @param roles the roles of the fictive authenticated user
     */
    protected void mockAuthenticated(String login, List<RoleEnum> roles) {
        when(authenticationServiceMock.getCurrentUserLoginOptional()).thenReturn(Optional.of(login));
        when(authenticationServiceMock.getCurrentUserLoginOrNull()).thenReturn(login);

        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (!roles.contains(roleEnum)) {
                doThrow(new AuthenticationRequiredException())
                        .when(authenticationServiceMock).assertCurrentUserHasRole(roleEnum);
            }
        }
    }

    /**
     * Mock an anonymous user, ie a user that is not connected
     */
    protected void mockAnonymousUser() {
        when(authenticationServiceMock.getCurrentUserLoginOptional()).thenReturn(Optional.empty());
        when(authenticationServiceMock.getCurrentUserLoginOrNull()).thenReturn(null);

        for (RoleEnum roleEnum : RoleEnum.values()) {
            doThrow(new AuthenticationRequiredException())
                    .when(authenticationServiceMock).assertCurrentUserHasRole(roleEnum);
        }
    }
}
