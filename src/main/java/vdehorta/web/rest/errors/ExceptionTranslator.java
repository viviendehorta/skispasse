package vdehorta.web.rest.errors;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import vdehorta.service.errors.*;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {



    @ExceptionHandler
    public ResponseEntity<Problem> handleDefaultRuntimeException(RuntimeException ex, NativeWebRequest request) {
        //Default method for all none-catched server exception : generate Server error problem
        InternalServerErrorAlertException problem = new InternalServerErrorAlertException("Server error!");
        return create(problem, request);
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BeanValidationAlertException problem = new BeanValidationAlertException(ex.getBindingResult().getFieldErrors());
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withTitle(Status.NOT_FOUND.getReasonPhrase())
                .withStatus(Status.NOT_FOUND)
                .withDetail(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withTitle(Status.CONFLICT.getReasonPhrase())
                .withStatus(Status.CONFLICT)
                .withDetail(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailAlreadyUsedException(vdehorta.service.errors.EmailAlreadyUsedException ex, NativeWebRequest request) {
        EmailAlreadyUsedException problem = new EmailAlreadyUsedException();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUsernameAlreadyUsedException(LoginAlreadyUsedException ex, NativeWebRequest request) {
        LoginAlreadyUsedAlertException problem = new LoginAlreadyUsedAlertException();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInvalidPasswordException(vdehorta.service.errors.InvalidPasswordException ex, NativeWebRequest request) {
        return create(new InvalidPasswordException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNewsFactNotFoundException(NewsFactNotFoundException ex, NativeWebRequest request) {
        NewsFactNotFoundAlertException problem = new NewsFactNotFoundAlertException(ex.getId());
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNewsFactAccessForbiddenException(NewsFactAccessForbiddenException ex, NativeWebRequest request) {
        //Generate a NOT FOUND http response like "not found in your news facts"
        NewsFactNotFoundAlertException problem = new NewsFactNotFoundAlertException(ex.getNewsFactId());
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleAuthenticationRequiredException(vdehorta.service.errors.AuthenticationRequiredException ex, NativeWebRequest request) {
        AuthenticationRequiredAlertException problem = new AuthenticationRequiredAlertException();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleMissingRoleException(MissingRoleException ex, NativeWebRequest request) {
        MissingRoleAlertException problem = new MissingRoleAlertException(ex.getRequiredRole());
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUserNotFoundException(UserNotFoundException ex, NativeWebRequest request) {
        UserNotFoundAlertException problem = new UserNotFoundAlertException(ex.getLogin());
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNewsFactVideoNotFoundException(NewsFactVideoNotFoundException ex, NativeWebRequest request) {
        //Return a 500 and not a not found because due to desynchronization between news fact and video
        InternalServerErrorAlertException problem = new InternalServerErrorAlertException("Error while accessing video of news fact with id '" + ex.getNewsFactId() + "'!");
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUnsupportedFileContentTypeException(UnsupportedFileContentTypeException ex, NativeWebRequest request) {
        UnsupportedMediaTypeAlertException problem = new UnsupportedMediaTypeAlertException("Unsuppported media type '" + ex.getContentType() + "'!");
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNewsCategoryNotFoundException(NewsCategoryNotFoundException ex, NativeWebRequest request) {
        NewsCategoryNotFoundAlertException problem = new NewsCategoryNotFoundAlertException(ex.getId());
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleMapStyleNotFoundException(MapStyleNotFoundException ex, NativeWebRequest request) {
        InternalServerErrorAlertException problem = new InternalServerErrorAlertException("Error getting application map style!");
        return create(problem, request);
    }
}
