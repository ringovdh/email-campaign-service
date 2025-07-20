package be.yorian.emailcampaignservice.mother;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.enums.EmailCampaignStatus;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;

import java.time.LocalDateTime;
import java.util.List;

import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
import static java.time.LocalDateTime.now;

public class EmailCampaignMother {

    public static EmailCampaign newEmailCampaign(LocalDateTime createdAt, List<Contact> contacts) {
        EmailCampaign emailCampaign = new EmailCampaign();
        emailCampaign.setName("Test email Campaign");
        emailCampaign.setTemplate(newSavedEmailTemplate(now()));
        emailCampaign.setContacts(contacts);
        emailCampaign.setScheduledAt(createdAt.plusDays(5));
        emailCampaign.setStatus(EmailCampaignStatus.DRAFT);
        return emailCampaign;
    }

    public static EmailCampaign newSavedEmailCampaign(LocalDateTime createdAt, List<Contact> contacts) {
        EmailCampaign savedEmailCampaign = newEmailCampaign(createdAt, contacts);
        savedEmailCampaign.setId(1L);
        savedEmailCampaign.setCreatedAt(createdAt);
        return savedEmailCampaign;
    }

    public static EmailCampaign newSavedEmailCampaign2(LocalDateTime createdAt, List<Contact> contacts) {
        EmailCampaign savedEmailCampaign = newEmailCampaign(createdAt, contacts);
        savedEmailCampaign.setId(2L);
        savedEmailCampaign.setCreatedAt(createdAt);
        return savedEmailCampaign;
    }

    public static EmailCampaignDTO newEmailCampaignDTO(LocalDateTime createdAt, List<Long> contacts) {
        return new EmailCampaignDTO(
                0L,
                "Test email Campaign",
                1L,
                contacts,
                null,
                createdAt.plusDays(5),
                null,
                null
        );
    }

    public static EmailCampaignDTO newInvalidEmailCampaignDTO() {
        return new EmailCampaignDTO(
                0L,
                "",
                null,
                List.of(),
                null,
                null,
                null,
                null
        );
    }

    public static EmailCampaignDTO savedEmailCampaignDTO(LocalDateTime createdAt, List<Long> contacts) {
        return new EmailCampaignDTO(
                1L,
                "Test email Campaign",
                1L,
                contacts,
                EmailCampaignStatus.DRAFT,
                createdAt.plusDays(5),
                createdAt,
                null
        );
    }

    public static EmailCampaignDTO savedEmailCampaignDTO2(LocalDateTime createdAt, List<Long> contacts) {
        return new EmailCampaignDTO(
                5L,
                "Other test email Campaign",
                1L,
                contacts,
                EmailCampaignStatus.DRAFT,
                createdAt.plusDays(5),
                createdAt,
                null
        );
    }

    public static EmailCampaignDTO updatedEmailCampaignDTO(LocalDateTime createdAt, List<Long> contacts) {
        return new EmailCampaignDTO(
                1L,
                "Test email Campaign",
                1L,
                contacts,
                EmailCampaignStatus.DRAFT,
                createdAt.plusDays(5),
                createdAt,
                now()
        );
    }


}
