package vdehorta.web.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vdehorta.EntityTestUtil;
import vdehorta.SkispasseApp;
import vdehorta.config.Constants;
import vdehorta.domain.User;
import vdehorta.dto.PasswordChangeDTO;
import vdehorta.dto.UserDto;
import vdehorta.repository.AuthorityRepository;
import vdehorta.repository.UserRepository;
import vdehorta.security.RoleEnum;
import vdehorta.service.AuthenticationService;
import vdehorta.service.ClockService;
import vdehorta.service.UserService;
import vdehorta.web.rest.errors.ExceptionTranslator;
import vdehorta.web.rest.vm.ManagedUserVM;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static vdehorta.EntityTestUtil.*;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@SpringBootTest(classes = SkispasseApp.class)
public class AccountResourceITest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ClockService clockService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpMessageConverter<?>[] httpMessageConverters;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restMvc;

    private MockMvc restUserMockMvc;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        MockitoAnnotations.initMocks(this);
        AccountResource accountResource =
                new AccountResource(userRepository, userService, authenticationService);

        AccountResource accountUserMockResource =
                new AccountResource(userRepository, userService, authenticationService);
        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource)
                .setMessageConverters(httpMessageConverters)
                .setControllerAdvice(exceptionTranslator)
                .build();
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource)
                .setControllerAdvice(exceptionTranslator)
                .build();
    }

    @Test
    public void getAuthenticated_shouldReturnNotAuthenticatedDtoIfUserIsNotAuthenticated() throws Exception {
        ResultActions resultActions = restUserMockMvc.perform(get("/account/authentication")
                .accept(MediaType.APPLICATION_JSON));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.authenticated").value(false))
                .andExpect(jsonPath("$.user").isEmpty());
    }

    @Test
    @WithMockUser(username = "marcopolo")
    public void getAuthenticated_caseOk() throws Exception {

        String login = "marcopolo";

        //Initialize database
        User user = EntityTestUtil.createDefaultUser();
        user.setLogin(login);
        userRepository.save(user);

        ResultActions resultActions = restUserMockMvc.perform(get("/account/authentication")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.user.login").value(login))
                .andExpect(jsonPath("$.user.firstName").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.user.lastName").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.user.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.user.imageUrl").value(DEFAULT_IMAGEURL))
                .andExpect(jsonPath("$.user.langKey").value(DEFAULT_LANGKEY))
                .andExpect(jsonPath("$.user.authorities").value(RoleEnum.USER.getValue()));
    }

    @Test
    @WithMockUser("save-account")
    public void updateAccount_caseOk() throws Exception {
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
        userDTO.setAuthorities(Collections.singleton(RoleEnum.ADMIN.getValue()));

        ResultActions resultActions = restMvc.perform(put("/account")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userDTO)));

        resultActions.andExpect(status().isOk());

        //Check database
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
    @WithMockUser("save-invalid-email")
    public void updateAccount_shouldThrowBadRequestIfMailIsMalformed() throws Exception {
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
        userDTO.setAuthorities(Collections.singleton(RoleEnum.ADMIN.getValue()));

        ResultActions resultActions = restMvc.perform(put("/account")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userDTO)));

        resultActions.andExpect(status().isBadRequest());

        assertThat(userRepository.findOneByEmailIgnoreCase("invalid email")).isNotPresent();
    }

    @Test
    @WithMockUser("save-existing-email")
    public void updateAccount_shouldThrowBadRequestIfMailIsAlreadyUsed() throws Exception {
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
        userDTO.setAuthorities(Collections.singleton(RoleEnum.ADMIN.getValue()));

        ResultActions resultActions = restMvc.perform(put("/account")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userDTO)));

        resultActions.andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
        assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");

    }

    @Test
    @WithMockUser("change-password-wrong-existing-password")
    public void changePassword_shouldThrowBadRequestForWrongExistingPassword() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-wrong-existing-password");
        user.setEmail("change-password-wrong-existing-password@example.com");
        userRepository.save(user);

        restMvc.perform(post("/account/change-password")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO("1" + currentPassword, "new password"))))
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-wrong-existing-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(currentPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser("change-password")
    public void changePassword_caseOk() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password");
        user.setEmail("change-password@example.com");
        userRepository.save(user);

        restMvc.perform(post("/account/change-password")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, "new password"))))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findOneByLogin("change-password").orElse(null);
        assertThat(passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser("change-password-too-small")
    public void changePassword_shouldThrowBadRequestForTooShortPassword() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-small");
        user.setEmail("change-password-too-small@example.com");
        userRepository.save(user);

        String newPassword = RandomStringUtils.random(ManagedUserVM.PASSWORD_MIN_LENGTH - 1);

        restMvc.perform(post("/account/change-password")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, newPassword))))
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-too-small").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @WithMockUser("change-password-too-long")
    public void changePassword_shouldThrowBadRequestForTooLongPassword() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-too-long");
        user.setEmail("change-password-too-long@example.com");
        userRepository.save(user);

        String newPassword = RandomStringUtils.random(ManagedUserVM.PASSWORD_MAX_LENGTH + 1);

        restMvc.perform(post("/account/change-password")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, newPassword))))
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-too-long").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @WithMockUser("change-password-empty")
    public void changePassword_shouldThrowBadRequestForEmptyPassword() throws Exception {
        User user = new User();
        String currentPassword = RandomStringUtils.random(60);
        user.setPassword(passwordEncoder.encode(currentPassword));
        user.setLogin("change-password-empty");
        user.setEmail("change-password-empty@example.com");
        userRepository.save(user);

        restMvc.perform(post("/account/change-password")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new PasswordChangeDTO(currentPassword, ""))))
                .andExpect(status().isBadRequest());

        User updatedUser = userRepository.findOneByLogin("change-password-empty").orElse(null);
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }
}
