package be.yorian.emailcampaignservice.mother;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;

import java.util.Date;

public class EmailTemplateMother {

    public static EmailTemplate newEmailTemplate() {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setName("test email template");
        emailTemplate.setSubject("This is the subject");
        emailTemplate.setBodyHtml("Email <b>content</b> comes here.");
        return  emailTemplate;
    }

    public static EmailTemplate newSavedEmailTemplate(Date createdAt) {
        EmailTemplate savedEmailTemplate = newEmailTemplate();
        savedEmailTemplate.setId(1L);
        savedEmailTemplate.setCreatedAt(createdAt);
        return  savedEmailTemplate;
    }

    public static EmailTemplateDTO newEmailTemplateDTO() {
        return new EmailTemplateDTO(
                0L,
                "test email template",
                "This is the subject",
                "Email <b>content</b> comes here.",
                null,
                null);
    }

    public static EmailTemplateDTO newInvalidEmailTemplateDTO() {
        return new EmailTemplateDTO(
                0L,
                "",
                null,
                "Email <b>content</b> comes here.",
                null,
                null);
    }

    public static EmailTemplateDTO savedEmailTemplateDTO(Date createdAt) {
        return new EmailTemplateDTO(
                5L,
                "test email template",
                "This is the subject",
                "Email <b>content</b> comes here.",
                createdAt,
                null);
    }
}
