package be.yorian.emailcampaignservice.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class EmailCampaignServiceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(EmailCampaignServiceExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EmailCampaignServiceErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        EmailCampaignServiceErrorResponse errorResponse = new EmailCampaignServiceErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmailCampaignServiceErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        EmailCampaignServiceErrorResponse errorResponse = new EmailCampaignServiceErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                handleErrorList(ex));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<EmailCampaignServiceErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        EmailCampaignServiceErrorResponse errorResponse = new EmailCampaignServiceErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EmailCampaignServiceErrorResponse> handleException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);

        EmailCampaignServiceErrorResponse errorResponse = new EmailCampaignServiceErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal server error has occurred."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private static List<String> handleErrorList(MethodArgumentNotValidException ex) {
        return  ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
    }
}
