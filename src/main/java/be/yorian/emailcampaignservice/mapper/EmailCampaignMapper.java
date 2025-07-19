package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;

import java.util.List;

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

    private static List<Long> mapToContactIDs(List<Contact> contacts) {
        if (contacts != null) {
            return contacts.stream()
                    .map(contact -> contact.getId())
                    .toList();
        }
        return List.of();
    }

}
