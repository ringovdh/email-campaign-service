package be.yorian.emailcampaignservice.mother;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;

import java.time.LocalDateTime;

public class EmailTemplateMother {

    public static EmailTemplate newEmailTemplate() {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setName("test email template");
        emailTemplate.setSubject("This is the subject");
        emailTemplate.setBodyHtml("Email <b>content</b> comes here.");
        return  emailTemplate;
    }

    public static EmailTemplate newSavedEmailTemplate(LocalDateTime createdAt) {
        EmailTemplate savedEmailTemplate = newEmailTemplate();
        savedEmailTemplate.setId(1L);
        savedEmailTemplate.setCreatedAt(createdAt);
        return  savedEmailTemplate;
    }

    public static EmailTemplate updatedEmailTemplate(LocalDateTime createdAt, LocalDateTime updatedAt) {
        EmailTemplate updatedEmailTemplate = newSavedEmailTemplate(createdAt);
        updatedEmailTemplate.setSubject("This is the updated subject");
        updatedEmailTemplate.setUpdatedAt(updatedAt);
        return  updatedEmailTemplate;
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

    public static EmailTemplateDTO savedEmailTemplateDTO(LocalDateTime createdAt) {
        return new EmailTemplateDTO(
                5L,
                "test email template",
                "This is the subject",
                "Email <b>content</b> comes here.",
                createdAt,
                null);
    }

    public static EmailTemplateDTO updatedEmailTemplateDTO(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new EmailTemplateDTO(
                5L,
                "test email template",
                "This is an updated subject",
                "Email <b>content</b> comes here.",
                createdAt,
                updatedAt);
    }

    public static EmailTemplateDTO invalidUpdatedEmailTemplateDTO(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new EmailTemplateDTO(
                5L,
                "",
                null,
                "Email <b>content</b> comes here.",
                createdAt,
                updatedAt);
    }
}
