package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;

import java.util.List;

import static java.time.LocalDateTime.now;

public final class EmailCampaignMapper {

    public static EmailCampaign mapToEmailCampaign(EmailCampaignDTO emailCampaignDTO) {
        if (emailCampaignDTO != null) {
            EmailCampaign emailCampaign = new EmailCampaign();
            emailCampaign.setName(emailCampaignDTO.name());
            emailCampaign.setStatus(emailCampaignDTO.status());
            emailCampaign.setScheduledAt(emailCampaignDTO.scheduledAt());
            return emailCampaign;
        }
        return null;
    }

    public static EmailCampaignDTO mapToEmailCampaignDTO(EmailCampaign emailCampaign) {
        if (emailCampaign != null) {
            return new EmailCampaignDTO(
                    emailCampaign.getId(),
                    emailCampaign.getName(),
                    emailCampaign.getTemplate().getId(),
                    mapToContactIDs(emailCampaign.getContacts()),
                    emailCampaign.getStatus(),
                    emailCampaign.getScheduledAt(),
                    emailCampaign.getCreatedAt(),
                    emailCampaign.getUpdatedAt());
        }
        return null;
    }

    public static void updateEmailCampaignFromDTO(EmailCampaign emailCampaign, EmailCampaignDTO updatedEmailCampaignDTO ) {
        if (emailCampaign == null || updatedEmailCampaignDTO == null) {
            return;
        }
        emailCampaign.setName(updatedEmailCampaignDTO.name());
        emailCampaign.setStatus(updatedEmailCampaignDTO.status());
        emailCampaign.setScheduledAt(updatedEmailCampaignDTO.scheduledAt());
        emailCampaign.setCreatedAt(updatedEmailCampaignDTO.createdAt());
        emailCampaign.setUpdatedAt(now());
    }


    private static List<Long> mapToContactIDs(List<Contact> contacts) {
        if (contacts != null) {
            return contacts.stream()
                    .map(contact -> contact.getId())
                    .toList();
        }
        return List.of();
    }

}
