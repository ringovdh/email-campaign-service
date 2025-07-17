package be.yorian.emailcampaignservice.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class EmailCampaignServiceExceptionHandler {

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


    private static List<String> handleErrorList(MethodArgumentNotValidException ex) {
        return  ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
    }
}
