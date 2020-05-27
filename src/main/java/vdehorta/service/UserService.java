package vdehorta.service;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vdehorta.config.Constants;
import vdehorta.domain.Authority;
import vdehorta.domain.User;
import vdehorta.dto.UserDto;
import vdehorta.repository.AuthorityRepository;
import vdehorta.repository.UserRepository;
import vdehorta.service.errors.EmailAlreadyUsedException;
import vdehorta.service.errors.InvalidPasswordException;
import vdehorta.service.errors.LoginAlreadyUsedException;
import vdehorta.service.errors.UserNotFoundException;
import vdehorta.service.util.RandomUtil;
import vdehorta.web.rest.vm.ManagedUserVM;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ClockService clockService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthorityRepository authorityRepository,
                       ClockService clockService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.clockService = clockService;
    }

    public UserDto createUser(UserDto userDTO) {
        log.debug("Creating user {}", userDTO);

        String login = userDTO.getLogin();
        String email = userDTO.getEmail().toLowerCase();
        if (userRepository.findOneByLogin(login.toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException(login);
        } else if (userRepository.findOneByEmailIgnoreCase(email).isPresent()) {
            throw new EmailAlreadyUsedException(email);
        }

        User user = new User();
        user.setLogin(login.toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(email.toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());

        final LocalDateTime now = clockService.now();

        user.setCreatedDate(now);
        user.setLastModifiedDate(now);

        user.setResetDate(now);
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        User createdUser = userRepository.save(user);
        log.debug("Created Information for User: {}", createdUser);
        return new UserDto(createdUser);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param login     login to identify the user
     * @param firstName to update first name
     * @param lastName  to update last name
     * @param email     to update email
     * @param langKey   to update lang key
     * @param imageUrl  to update image url
     * @throws UserNotFoundException     if the user cannot be found in persistence
     * @throws EmailAlreadyUsedException if the given email is already used
     */
    public void updateUser(String login, String firstName, String lastName, String email, String langKey, String imageUrl) throws UserNotFoundException, EmailAlreadyUsedException {
        log.debug("Updating basic user info of {}", login);
        String loginLowerCase = login.toLowerCase();
        User user = userRepository.findOneByLogin(loginLowerCase).orElseThrow(() -> new UserNotFoundException(login));
        String lowerCaseEmail = email.toLowerCase();

        Optional<User> maySameEmailUser = userRepository.findOneByEmailIgnoreCase(lowerCaseEmail);
        if (maySameEmailUser.isPresent() && !maySameEmailUser.get().getLogin().equals(loginLowerCase)) {
            throw new EmailAlreadyUsedException(email);
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(lowerCaseEmail);
        user.setLangKey(langKey);
        user.setImageUrl(imageUrl);
        userRepository.save(user);
        log.debug("Changed basic information for user: {}", user);
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public UserDto updateUser(UserDto userDTO) throws UserNotFoundException {
        Preconditions.checkNotNull(userDTO.getId(), "Id of user to update should not be null!");
        String userId = userDTO.getId();

        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        String email = userDTO.getEmail().toLowerCase();
        Optional<User> maySameEmailUser = userRepository.findOneByEmailIgnoreCase(email);

        //Check that new email is not already used by another user
        if (maySameEmailUser.isPresent() && (!maySameEmailUser.get().getId().equals(userId))) {
            throw new EmailAlreadyUsedException(email);
        }

        String login = userDTO.getLogin().toLowerCase();
        Optional<User> maySameLoginUser = userRepository.findOneByLogin(login);
        if (maySameLoginUser.isPresent() && (!maySameLoginUser.get().getId().equals(userId))) {
            throw new LoginAlreadyUsedException(login);
        }

        existingUser.setLogin(login);
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(email);
        existingUser.setImageUrl(userDTO.getImageUrl());
        existingUser.setActivated(userDTO.isActivated());
        existingUser.setLangKey(userDTO.getLangKey());
        Set<Authority> managedAuthorities = existingUser.getAuthorities();
        managedAuthorities.clear();
        userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(managedAuthorities::add);
        User updatedUser = userRepository.save(existingUser);
        log.debug("Changed Information for User: {}", updatedUser);
        return new UserDto(updatedUser);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword, String updaterLogin) throws InvalidPasswordException, UserNotFoundException {
        log.debug("Updating password for user: {}", updaterLogin);

        if (!checkPasswordLength(newPassword)) {
            throw new InvalidPasswordException();
        }

        User user = userRepository.findOneByLogin(updaterLogin).orElseThrow(() -> new UserNotFoundException(updaterLogin));

        String currentEncryptedPassword = user.getPassword();
        if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
            throw new InvalidPasswordException();
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        log.debug("Changed password for User: {}", user);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDto::new);
    }

    public UserDto getUser(String login) throws UserNotFoundException {
        User user = userRepository.findOneByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        return new UserDto(user);
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
