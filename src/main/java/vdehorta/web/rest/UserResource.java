package vdehorta.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vdehorta.config.ApplicationProperties;
import vdehorta.domain.User;
import vdehorta.dto.UserDto;
import vdehorta.security.RoleEnum;
import vdehorta.service.AuthenticationService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.BadRequestAlertException;
import vdehorta.web.rest.errors.EmailAlreadyUsedException;
import vdehorta.web.rest.errors.LoginAlreadyUsedAlertException;
import vdehorta.web.rest.util.HeaderUtil;
import vdehorta.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/users")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private String applicationName;

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserResource(ApplicationProperties applicationProperties, UserService userService, AuthenticationService authenticationService) {
        this.applicationName = applicationProperties.getClientAppName();
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * {@code POST  /user}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDTO) throws URISyntaxException {
        log.debug("REST request to create a user : {}", userDTO);

        authenticationService.assertAuthenticationRole(RoleEnum.ADMIN);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an id!");
        }

        UserDto newUser = userService.createUser(userDTO);
        return ResponseEntity
                .created(new URI("/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlertHeaders(applicationName, "User '" + newUser.getLogin() + "' was created."))
                .body(newUser);
    }

    /**
     * {@code PUT /user} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException      {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedAlertException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDTO) {
        log.debug("REST request to update a user: {}", userDTO);

        authenticationService.assertAuthenticationRole(RoleEnum.ADMIN);
        UserDto updatedUser = userService.updateUser(userDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlertHeaders(applicationName, "User '" + updatedUser.getLogin() + "' was updated."))
                .body(updatedUser);
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(Pageable pageable) {
        log.debug("REST request to get all users");

        authenticationService.assertAuthenticationRole(RoleEnum.ADMIN);
        final Page<UserDto> page = userService.getAllUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(page.getContent());
    }

    /**
     * {@code GET /users/roles} : get a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GetMapping("/roles")
    public List<String> getRoles() {
        log.debug("REST request to get list of all roles");

        authenticationService.assertAuthenticationRole(RoleEnum.ADMIN);
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matching user, or with status {@code 403 (Bad Request)}.
     */
    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);

        authenticationService.assertAuthenticationRole(RoleEnum.ADMIN);
        return userService.getUser(login);
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);

        authenticationService.assertAuthenticationRole(RoleEnum.ADMIN);
        userService.deleteUser(login);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createAlertHeaders(applicationName, "User '" + login + "' was deleted."))
                .build();
    }
}
