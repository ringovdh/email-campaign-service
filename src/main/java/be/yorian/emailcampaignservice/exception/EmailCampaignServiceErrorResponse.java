package be.yorian.emailcampaignservice.exception;

import java.util.List;

public class EmailCampaignServiceErrorResponse {

    private final int errorCode;
    private String message;
    private List<String> validationErrors;

    public EmailCampaignServiceErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public EmailCampaignServiceErrorResponse(int errorCode, List<String> validationErrors) {
        this.errorCode = errorCode;
        this.validationErrors = validationErrors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

}
