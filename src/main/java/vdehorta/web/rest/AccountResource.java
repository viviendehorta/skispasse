package vdehorta.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import vdehorta.bean.dto.AuthenticationDto;
import vdehorta.bean.dto.PasswordChangeDTO;
import vdehorta.bean.dto.UserDto;
import vdehorta.repository.UserRepository;
import vdehorta.security.RoleEnum;
import vdehorta.service.AuthenticationService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.InvalidPasswordException;

import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/account")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AccountResource(
            UserRepository userRepository,
            UserService userService,
            AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * {@code GET  /authentication} : get the AuthenticatedDto matching the authenticated user.
     * If user is not authenticated, then AuthenticatedDto.isAuthenticated is false and AuthenticatedDto.user is null.
     *
     * @return the AuthenticatedDto matching the authenticated user.
     */
    @GetMapping(path = "/authentication")
    public AuthenticationDto getAuthenticationState() {
        log.debug("REST request to get the current authentication state");
        AuthenticationDto.Builder builder = new AuthenticationDto.Builder();
        Optional<String> mayAuthenticatedLogin = authenticationService.getCurrentUserLoginOptional();
        if (mayAuthenticatedLogin.isPresent()) {
            UserDto authenticatedUser = userService.getUser(mayAuthenticatedLogin.get());
            builder.isAuthenticated(true).user(authenticatedUser);
        }
        return builder.build();
    }

    /**
     * {@code PUT  /account} : update the current user basic information.
     *
     * @param userDTO the current user information.
     */
    @PutMapping
    public void updateAccountBasicInfo(@Valid @RequestBody UserDto userDTO) {
        log.debug("REST request to update the current account");

        authenticationService.assertAuthenticationRole(RoleEnum.USER);

        userService.updateUser(authenticationService.getCurrentUserLoginOrThrowError(), userDTO.getFirstName(),
                userDTO.getLastName(), userDTO.getEmail());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        log.debug("REST request to change the current user password");

        authenticationService.assertAuthenticationRole(RoleEnum.USER);

        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword(),
                authenticationService.getCurrentUserLoginOrThrowError());
    }
}
