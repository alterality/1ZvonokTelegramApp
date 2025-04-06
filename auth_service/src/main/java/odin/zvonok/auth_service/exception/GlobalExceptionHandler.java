package odin.zvonok.auth_service.exception;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials, please try again."),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppError> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.FORBIDDEN.value(), "Access Denied"),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<AppError> handleAuthException(AuthException ex) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }
}
