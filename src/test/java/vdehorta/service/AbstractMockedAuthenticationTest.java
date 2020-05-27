package vdehorta.service;

import org.mockito.Mockito;
import vdehorta.service.errors.AuthenticationRequiredException;

import java.util.Optional;

import static org.mockito.Mockito.when;

public abstract class AbstractMockedAuthenticationTest {

    protected static final String DEFAULT_AUTHENTICATED_USER_LOGIN = "Skisp'";

    protected AuthenticationService authenticationServiceMock;

    public void setup() {
        authenticationServiceMock = Mockito.mock(AuthenticationService.class);
    }

    /**
     * mockAuthenticated with login DEFAULT_AUTHENTICATED_USER_LOGIN
     */
    protected void mockAuthenticated() {
        mockAuthenticated(DEFAULT_AUTHENTICATED_USER_LOGIN);
    }

    /**
     * Mock public methods of AuthenticationService mock to act as if authenticated with given user login
     *
     * @param login fictive authenticated user login
     */
    protected void mockAuthenticated(String login) {
        when(authenticationServiceMock.getCurrentUserLoginOptional()).thenReturn(Optional.of(login));
        when(authenticationServiceMock.getCurrentUserLoginOrThrowError()).thenReturn(login);
    }

    /**
     * Mock an anonymous user, ie a user that is not connected
     */
    protected void mockAnonymousUser() {
        when(authenticationServiceMock.getCurrentUserLoginOptional()).thenReturn(Optional.empty());
        when(authenticationServiceMock.getCurrentUserLoginOrThrowError()).thenThrow(AuthenticationRequiredException.class);
    }
}
