package vdehorta.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import vdehorta.dto.PasswordChangeDTO;
import vdehorta.dto.UserDto;
import vdehorta.repository.UserRepository;
import vdehorta.security.RoleEnum;
import vdehorta.service.AuthenticationService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.InvalidPasswordException;

import javax.validation.Valid;

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
     * {@code GET  /account/authenticate} : check if the user is authenticated, and return its login.
     *
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated() {
        log.debug("REST request to check if the current user is authenticated");
        return authenticationService.getCurrentUserLoginOptional().orElse("");
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping
    public UserDto getAccount() {
        log.debug("REST request to get the current account");

        authenticationService.assertAuthenticationRole(RoleEnum.USER);
        return userService.getUser(authenticationService.getCurrentUserLoginOrThrowError());
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
                userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(), userDTO.getImageUrl());
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
