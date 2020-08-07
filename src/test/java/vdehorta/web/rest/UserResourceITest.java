package vdehorta.web.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.Problem;
import vdehorta.bean.dto.UserDto;
import vdehorta.config.ApplicationProperties;
import vdehorta.domain.Authority;
import vdehorta.domain.User;
import vdehorta.repository.AuthorityRepository;
import vdehorta.repository.UserRepository;
import vdehorta.security.RoleEnum;
import vdehorta.service.AuthenticationService;
import vdehorta.service.ClockService;
import vdehorta.service.mapper.UserMapper;
import vdehorta.utils.PersistenceTestUtils;
import vdehorta.web.rest.vm.ManagedUserVM;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static vdehorta.security.RoleEnum.*;
import static vdehorta.utils.BeanTestUtils.*;
import static vdehorta.utils.RestTestUtils.mockAnonymous;
import static vdehorta.utils.RestTestUtils.mockAuthentication;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceITest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClockService clockService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    private User user;


    @BeforeEach
    public void initTest() {
        PersistenceTestUtils.resetDatabase(mongoTemplate, applicationProperties);
        user = createDefaultUser();
        initAuthorityRepository();
        mockAnonymous(authenticationService);
    }

    @Test
    public void createUser_caseOk() {
        mockAuthentication(authenticationService, "user", ADMIN);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        UserDto inputUser = new UserDto();
        inputUser.setLogin(DEFAULT_LOGIN);
        inputUser.setFirstName(DEFAULT_FIRSTNAME);
        inputUser.setLastName(DEFAULT_LASTNAME);
        inputUser.setEmail(DEFAULT_EMAIL);
        inputUser.setAuthorities(Collections.singleton(CONTRIBUTOR.getValue()));
        inputUser.setActivated(true);

        //Then
        ResponseEntity<UserDto> response = testRestTemplate.postForEntity("/users", inputUser, UserDto.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        UserDto createdUser = response.getBody();
        assertThat(createdUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(createdUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(createdUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(createdUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(createdUser.getAuthorities()).containsExactlyInAnyOrder(USER.getValue(), CONTRIBUTOR.getValue());
        assertThat(createdUser.isActivated()).isTrue();

        // Validate the User in the database
        List<User> persistedUsers = userRepository.findAll();
        assertThat(persistedUsers).hasSize(databaseSizeBeforeCreate + 1);
        User persistedUser = persistedUsers.get(persistedUsers.size() - 1);
        assertThat(persistedUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(persistedUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(persistedUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(persistedUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(persistedUser.getPassword()).isNotEmpty();
        assertThat(persistedUser.getActivated()).isTrue();
    }

    @Test
    public void createUser_shouldThrowBadRequestIfLoginIsNotLowerCase() {
        mockAuthentication(authenticationService, "user", ADMIN);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin("Mandela");
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail(DEFAULT_EMAIL);
        managedUserVM.setActivated(true);
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        //When
        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/users", managedUserVM, Problem.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Some data is invalid : Value 'Mandela' is invalid for field 'login'!");

        assertThat(userRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void createUser_shouldThrowBadRequestIfIdAlreadyExists() {
        mockAuthentication(authenticationService, "user", ADMIN);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(DEFAULT_USER_ID); // Existing id
        managedUserVM.setLogin(DEFAULT_LOGIN);
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail(DEFAULT_EMAIL);
        managedUserVM.setActivated(true);
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        // An entity with an existing ID cannot be created, so this API call must fail
        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/users", managedUserVM, Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("A new user cannot already have an id!");

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void createUser_shouldThrowBadRequestIfLoginAlreadyExists() {

        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin(DEFAULT_LOGIN);// login is already used
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail("anothermail@localhost");
        managedUserVM.setActivated(true);
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/users", managedUserVM, Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Login name already used!");

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void createUser_shouldThrowBadRequestIfEmailAlreadyExists() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin("anotherlogin");
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail(DEFAULT_EMAIL);// this email is already used
        managedUserVM.setActivated(true);
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/users", managedUserVM, Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Email is already in use!");

        assertThat( userRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void createUser_shouldThrowUnauthorizedWhenUserIsNotConnected() {
        mockAnonymous(authenticationService);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        UserDto inputUser = new UserDto();
        inputUser.setLogin(DEFAULT_LOGIN);
        inputUser.setFirstName(DEFAULT_FIRSTNAME);
        inputUser.setLastName(DEFAULT_LASTNAME);
        inputUser.setEmail(DEFAULT_EMAIL);
        inputUser.setAuthorities(Collections.singleton(CONTRIBUTOR.getValue()));

        //Then
        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/users", inputUser, Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
        assertThat(response.getBody().getDetail()).isEqualTo("Authentication is required!");
        assertThat( userRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void createUser_shouldThrowForbiddenWhenUserIsNotAdmin() {
        mockAuthentication(authenticationService, "user", CONTRIBUTOR);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        UserDto inputUser = new UserDto();
        inputUser.setLogin(DEFAULT_LOGIN);
        inputUser.setFirstName(DEFAULT_FIRSTNAME);
        inputUser.setLastName(DEFAULT_LASTNAME);
        inputUser.setEmail(DEFAULT_EMAIL);
        inputUser.setAuthorities(Collections.singleton(CONTRIBUTOR.getValue()));

        //Then
        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/users", inputUser, Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody().getDetail()).isEqualTo("Role 'administrator' is required!");
        assertThat( userRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllUsers_caseOk() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);

        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity("/users/all", UserDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(1);
        UserDto userDto = response.getBody()[0];
        assertThat(userDto.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDto.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDto.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDto.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void getUser_caseOk() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);

        ResponseEntity<UserDto> response = testRestTemplate.getForEntity("/users/" + user.getLogin(), UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        UserDto userDto = response.getBody();
        assertThat(userDto.getLogin()).isEqualTo(user.getLogin());
        assertThat(userDto.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDto.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDto.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void getUser_shouldThrowNotFoundIfLoginDoesntExist() {
        mockAuthentication(authenticationService, "user", ADMIN);
        ResponseEntity<Problem> response = testRestTemplate.getForEntity("/users/unknown", Problem.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void updateUser_adminShouldSucceedUpdatingAllBasicFields() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);
        int initialCount = userRepository.findAll().size();

        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin(updatedUser.getLogin());
        managedUserVM.setPassword(UPDATED_PASSWORD);
        managedUserVM.setFirstName(UPDATED_FIRSTNAME);
        managedUserVM.setLastName(UPDATED_LASTNAME);
        managedUserVM.setEmail(UPDATED_EMAIL);
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setCreatedBy(updatedUser.getCreatedBy());
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        ResponseEntity<Object> response = testRestTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(managedUserVM), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(initialCount);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    public void updateUser_adminShouldSucceedUpdatingLogin() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin(UPDATED_LOGIN);
        managedUserVM.setPassword(UPDATED_PASSWORD);
        managedUserVM.setFirstName(UPDATED_FIRSTNAME);
        managedUserVM.setLastName(UPDATED_LASTNAME);
        managedUserVM.setEmail(UPDATED_EMAIL);
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setCreatedBy(updatedUser.getCreatedBy());
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        ResponseEntity<Object> response = testRestTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(managedUserVM), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    public void updateUser_shouldThrowBadRequestIfEmailIsAlreadyUsed() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);

        User anotherUser = new User();
        anotherUser.setLogin("login");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("skispasse@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        userRepository.save(anotherUser);

        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin(updatedUser.getLogin());
        managedUserVM.setPassword(updatedUser.getPassword());
        managedUserVM.setFirstName(updatedUser.getFirstName());
        managedUserVM.setLastName(updatedUser.getLastName());
        managedUserVM.setEmail("skispasse@localhost");// this email should already be used by anotherUser
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setCreatedBy(updatedUser.getCreatedBy());
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        ResponseEntity<Problem> response = testRestTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(managedUserVM), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Email is already in use!");
    }

    @Test
    public void updateUser_shouldThrowBadRequestIfLoginIsAlreadyUsed() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);
        User anotherUser = new User();
        anotherUser.setLogin("another_login");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("skispasse@localhost");
        anotherUser.setFirstName("FirstName");
        anotherUser.setLastName("LastName");
        userRepository.save(anotherUser);

        final int initialCount = userRepository.findAll().size();


        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin("another_login");// this login should already be used by anotherUser
        managedUserVM.setPassword(updatedUser.getPassword());
        managedUserVM.setFirstName(updatedUser.getFirstName());
        managedUserVM.setLastName(updatedUser.getLastName());
        managedUserVM.setEmail(updatedUser.getEmail());
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setCreatedBy(updatedUser.getCreatedBy());
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(USER.getValue()));

        ResponseEntity<Problem> response = testRestTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(managedUserVM), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody().getDetail()).isEqualTo("Login name already used!");

        assertThat(userRepository.findAll()).hasSize(initialCount);
    }

    @Test
    public void deleteUser_caseOk() {
        mockAuthentication(authenticationService, "user", ADMIN);

        userRepository.save(user);
        int initialCount = userRepository.findAll().size();

        ResponseEntity<Object> response = testRestTemplate.exchange("/users/" + user.getLogin(), HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        assertThat(userRepository.findAll()).hasSize(initialCount - 1);
    }

    @Test
    public void getAllRoles_caseOk() {
        mockAuthentication(authenticationService, "user", ADMIN);

        ResponseEntity<String[]> response = testRestTemplate.getForEntity("/users/roles", String[].class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .hasSize(3)
                .containsExactlyInAnyOrder(USER.getValue(), ADMIN.getValue(), CONTRIBUTOR.getValue());
    }

    @Test
    public void testUserEquals() throws Exception {
        equalsVerifier(User.class);
        User user1 = new User();
        user1.setId("id1");
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId("id2");
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    public void testUserDTOtoUser() {
        UserDto userDTO = new UserDto();
        userDTO.setId(DEFAULT_USER_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(USER.getValue()));
        userDTO.setCreatedDate(DEFAULT_CREATED_DATE);
        userDTO.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getActivated()).isEqualTo(true);
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(USER.getValue());
    }

    @Test
    public void testUserToUserDTO() {
        user.setId(DEFAULT_USER_ID);
        user.setCreatedBy(DEFAULT_LOGIN);
        user.setCreatedDate(clockService.now());
        user.setLastModifiedBy(DEFAULT_LOGIN);
        user.setLastModifiedDate(clockService.now());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(USER.getValue());
        authorities.add(authority);
        user.setAuthorities(authorities);

        UserDto userDTO = userMapper.userToUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isEqualTo(true);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(USER.getValue());
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    public void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isEqualTo(authorityA);
        assertThat(authorityA).isNotEqualTo(null);
        assertThat(authorityA).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isEqualTo(0);
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(ADMIN.getValue());
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(USER.getValue());
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(USER.getValue());
        assertThat(authorityA).isEqualTo(authorityB);
        assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
    }

    private void initAuthorityRepository() {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            Authority authority = new Authority();
            authority.setName(roleEnum.getValue());
            authorityRepository.save(authority);
        }
    }
}
