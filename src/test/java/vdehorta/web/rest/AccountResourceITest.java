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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zalando.problem.Problem;
import vdehorta.bean.dto.AuthenticationDto;
import vdehorta.bean.dto.PasswordChangeDTO;
import vdehorta.bean.dto.UserDto;
import vdehorta.config.Constants;
import vdehorta.domain.User;
import vdehorta.repository.UserRepository;
import vdehorta.service.AuthenticationService;
import vdehorta.utils.BeanTestUtils;
import vdehorta.utils.PersistenceTestUtils;
import vdehorta.web.rest.vm.ManagedUserVM;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static vdehorta.security.RoleEnum.ADMIN;
import static vdehorta.security.RoleEnum.USER;
import static vdehorta.utils.BeanTestUtils.*;
import static vdehorta.utils.RestTestUtils.mockAnonymous;
import static vdehorta.utils.RestTestUtils.mockAuthentication;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountResourceITest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @BeforeEach
    public void setup() {
        PersistenceTestUtils.resetDatabase(mongoTemplate);
        mockAnonymous(authenticationService); //By default, mock anonymous authentication
    }

    @Test
    public void getAuthenticated_shouldReturnNotAuthenticatedDtoIfUserIsNotAuthenticated() {

        ResponseEntity<AuthenticationDto> response = testRestTemplate.getForEntity("/account/authentication", AuthenticationDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getAuthenticated()).isFalse();
        assertThat(response.getBody().getUser()).isNull();
    }

    @Test
    public void getAuthenticated_caseOk() {
        String login = "marcopolo";

        mockAuthentication(authenticationService, "marcopolo", USER);

        //Initialize database
        User user = BeanTestUtils.createDefaultUser();
        user.setLogin(login);
        userRepository.save(user);

        ResponseEntity<AuthenticationDto> response = testRestTemplate.getForEntity("/account/authentication", AuthenticationDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getAuthenticated()).isTrue();
        UserDto resultUser = response.getBody().getUser();
        assertThat(resultUser.getLogin()).isEqualTo(login);
        assertThat(resultUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(resultUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(resultUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(resultUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(resultUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(resultUser.getAuthorities()).containsOnly(USER.getValue());
    }

    @Test
    public void updateAccount_caseOk() throws Exception {
        mockAuthentication(authenticationService, "save-account", USER);

        User user = new User();
        user.setLogin("save-account");
        user.setEmail("save-account@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);

        userRepository.save(user);

        UserDto userDTO = new UserDto();
        userDTO.setLogin("not-used");
        userDTO.setFirstName("firstname");
        userDTO.setLastName("lastname");
        userDTO.setEmail("save-account@example.com");
        userDTO.setActivated(false);
        userDTO.setImageUrl("http://placehold.it/50x50");
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(ADMIN.getValue()));

        ResponseEntity<Object> response = testRestTemplate.exchange("/account", HttpMethod.PUT, new HttpEntity<>(userDTO), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);

        User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
        assertThat(updatedUser.getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(updatedUser.getLastName()).isEqualTo(userDTO.getLastName());
        assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(updatedUser.getLangKey()).isEqualTo(userDTO.getLangKey());
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(updatedUser.getImageUrl()).isEqualTo(userDTO.getImageUrl());
        assertThat(updatedUser.getActivated()).isEqualTo(true);
        assertThat(updatedUser.getAuthorities()).isEmpty();
    }

    @Test
    public void updateAccount_shouldThrowBadRequestIfMailIsMalformed() throws Exception {
        mockAuthentication(authenticationService, "save-invalid-email", USER);

        User user = new User();
        user.setLogin("save-invalid-email");
        user.setEmail("save-invalid-email@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);

        userRepository.save(user);

        UserDto userDTO = new UserDto();
        userDTO.setLogin("not-used");
        userDTO.setFirstName("firstname");
        userDTO.setLastName("lastname");
        userDTO.setEmail("invalid email");
        userDTO.setActivated(false);
        userDTO.setImageUrl("http://placehold.it/50x50");
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(ADMIN.getValue()));

        ResponseEntity<Problem> response = testRestTemplate.exchange("/account", HttpMethod.PUT, new HttpEntity<>(userDTO), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(userRepository.findOneByEmailIgnoreCase("invalid email")).isNotPresent();
    }

    @Test
    public void updateAccount_shouldThrowBadRequestIfMailIsAlreadyUsed() throws Exception {
        mockAuthentication(authenticationService, "save-existing-email", USER);

        User user = new User();
        user.setLogin("save-existing-email");
        user.setEmail("save-existing-email@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        userRepository.save(user);

        User anotherUser = new User();
        anotherUser.setLogin("save-existing-email2");
        anotherUser.setEmail("save-existing-email2@example.com");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        userRepository.save(anotherUser);

        UserDto userDTO = new UserDto();
        userDTO.setLogin("not-used");
        userDTO.setFirstName("firstname");
        userDTO.setLastName("lastname");
        userDTO.setEmail("save-existing-email2@example.com");
        userDTO.setActivated(false);
        userDTO.setImageUrl("http://placehold.it/50x50");
        userDTO.setLangKey(Constants.DEFAULT_LANGUAGE);
        userDTO.setAuthorities(Collections.singleton(ADMIN.getValue()));

        ResponseEntity<Problem> response = testRestTemplate.exchange("/account", HttpMethod.PUT, new HttpEntity<>(userDTO), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        User updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
        assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
    }

    @Test
    public void changePassword_shouldThrowBadRequestForWrongExistingPassword() throws Exception {
        mockAuthentication(authenticationService, "change-password-wrong-existing-password", USER);

        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-wrong-existing-password");
        user.setEmail("change-password-wrong-existing-password@example.com");
        userRepository.save(user);

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/account/change-password", new HttpEntity<>(new PasswordChangeDTO("1" + currentPassword, "new password")), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        User updatedUser = userRepository.findOneByLogin("change-password-wrong-existing-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(currentPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    public void changePassword_caseOk() {
        mockAuthentication(authenticationService, "change-password", USER);

        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password");
        user.setEmail("change-password@example.com");
        userRepository.save(user);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/account/change-password",
                new HttpEntity<>(new PasswordChangeDTO(currentPassword, "new password")),
                Object.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);

        User updatedUser = userRepository.findOneByLogin("change-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
    }

    @Test
    public void changePassword_shouldThrowBadRequestForTooShortPassword() throws Exception {
        mockAuthentication(authenticationService, "change-password-too-small", USER);

        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-small");
        user.setEmail("change-password-too-small@example.com");
        userRepository.save(user);

        String newPassword = RandomStringUtils.random(ManagedUserVM.PASSWORD_MIN_LENGTH - 1);

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/account/change-password", new HttpEntity<>(new PasswordChangeDTO(currentPassword, newPassword)), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        User updatedUser = userRepository.findOneByLogin("change-password-too-small").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void changePassword_shouldThrowBadRequestForTooLongPassword() {
        mockAuthentication(authenticationService, "change-password-too-long", USER);
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-long");
        user.setEmail("change-password-too-long@example.com");
        userRepository.save(user);

        String newPassword = RandomStringUtils.random(ManagedUserVM.PASSWORD_MAX_LENGTH + 1);

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/account/change-password", new HttpEntity<>(new PasswordChangeDTO(currentPassword, newPassword)), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        User updatedUser = userRepository.findOneByLogin("change-password-too-long").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void changePassword_shouldThrowBadRequestForEmptyPassword() throws Exception {
        mockAuthentication(authenticationService, "change-password-empty", USER);

        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-empty");
        user.setEmail("change-password-empty@example.com");
        userRepository.save(user);

        ResponseEntity<Problem> response = testRestTemplate.postForEntity("/account/change-password", new HttpEntity<>(new PasswordChangeDTO(currentPassword, "")), Problem.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

        User updatedUser = userRepository.findOneByLogin("change-password-empty").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }
}
