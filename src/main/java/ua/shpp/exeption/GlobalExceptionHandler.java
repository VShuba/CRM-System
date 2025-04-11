package ua.shpp.exeption;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Побудова відповіді про помилку з детальною інформацією.
     *
     * @param ex      екземпляр виключення
     * @param status  HTTP-статус, який буде встановлено у відповіді
     * @param message повідомлення про помилку
     * @param request HTTP-запит, що спричинив виключення
     * @return ResponseEntity з відповіддю про помилку та відповідним HTTP-статусом
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status,
                                                             String message, HttpServletRequest request) {
        log.error("{}: {}", message, ex.getMessage(), ex);

        ErrorResponse response = new ErrorResponse(message, ex.getMessage(), status.value(),
                request.getRequestURI(), LocalDateTime.now());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
                                                                       HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, "Unsuccessful login attempt", request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex,
                                                                HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UsernameNotFoundException ex,
                                                                     HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "User Not Found", request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
                                                                          HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, "User Already Exists", request);
    }
}
