package ua.shpp.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                   HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request) {

        String errorMessage = ex.getParameterValidationResults().stream()
                .flatMap(vr -> vr.getResolvableErrors().stream())
                .map(error -> error.getCodes() != null && error.getCodes().length > 0
                        ? error.getCodes()[0] + ": " + error.getDefaultMessage()
                        : error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, errorMessage, request);
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

    // for Organization

    @ExceptionHandler(OrganizationNotFound.class)
    public ResponseEntity<ErrorResponse> handleOrganizationNotFound(OrganizationNotFound ex,
                                                                    HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Organization Not Found", request);
    }

    @ExceptionHandler(OrganizationAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleOrganizationAlreadyExists(OrganizationAlreadyExists ex,
                                                                         HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Organization Already Exists", request);
    }

    // For Branch

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBranchNotFoundException(BranchNotFoundException ex,
                                                                       HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Branch Not Found in DB", request);
    }

    @ExceptionHandler(BranchOrganizationMismatchException.class)
    public ResponseEntity<ErrorResponse> handleBranchOrganizationMismatchException(BranchOrganizationMismatchException ex,
                                                                                   HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Branch Organization Mismatch", request);
    }

    @ExceptionHandler(BranchAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBranchAlreadyExistsException(BranchAlreadyExistsException ex,
                                                                            HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, "Branch Already Exists", request);
    }


    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOfferNotFound(OfferNotFoundException ex,
                                                             HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Offer not found", request);
    }

    // for event type

    @ExceptionHandler(EventTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventTypeNotFound(EventTypeNotFoundException ex,
                                                                 HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Event type not found", request);
    }

    @ExceptionHandler(EventTypeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventTypeAlreadyExists(EventTypeAlreadyExistsException ex,
                                                                      HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, "Event type Already Exists", request);
    }


    // for Invitation
    @ExceptionHandler(InvitationAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleInvitationAlreadyUsedException(
            InvitationAlreadyUsedException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(InvitationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInvitationNotFoundException(
            InvitationNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(InvitationExpiredException.class)
    public ResponseEntity<ErrorResponse> handleInvitationExpiredException(
            InvitationExpiredException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(WrongInvitationRecipient.class)
    public ResponseEntity<ErrorResponse> handleWrongInvitationRecipient(
            WrongInvitationRecipient ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    //For user organizations
    @ExceptionHandler(UserOrganizationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserOrganizationNotFoundException(
            UserOrganizationNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // for Client

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFound(ClientNotFoundException ex,
                                                              HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Client not found", request);
    }

    @ExceptionHandler(ClientOrganizationMismatchException.class)
    public ResponseEntity<ErrorResponse> handleClientOrganizationMismatch(ClientOrganizationMismatchException ex,
                                                                          HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Client-Organization Mismatch", request);
    }

    // for Employee
    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJsonFormatException(InvalidJsonFormatException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Invalid JSON format for EmployeeRequestDTO", request);
    }

    // for Event
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventException(EventNotFoundException ex,
                                                                       HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Event not found", request);
    }

    // for Deal
    @ExceptionHandler(DealNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventException(DealNotFoundException ex,
                                                              HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Deal not found", request);
    }

    @ExceptionHandler(VisitAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEventException(VisitAlreadyUsedException ex,
                                                              HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Visit already used", request);
    }
}