package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.ContactDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;

import java.util.List;

import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplate;
import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplateDTO;

public final class EmailCampaignMapper {

    public static EmailCampaign mapToEmailCampaign(EmailCampaignDTO emailCampaignDTO) {
        if (emailCampaignDTO != null) {
            EmailCampaign emailCampaign = new EmailCampaign();
            emailCampaign.setName(emailCampaignDTO.name());
            emailCampaign.setTemplate(mapToEmailTemplate(emailCampaignDTO.emailTemplate()));
            emailCampaign.setContacts(mapDtosToContacts(emailCampaignDTO.contacts()));
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
                    mapToEmailTemplateDTO(emailCampaign.getTemplate()),
                    mapContactsToDTOs(emailCampaign.getContacts()),
                    emailCampaign.getStatus(),
                    emailCampaign.getScheduledAt(),
                    emailCampaign.getCreatedAt(),
                    emailCampaign.getUpdatedAt());
        }
        return null;
    }

    private static List<ContactDTO> mapContactsToDTOs(List<Contact> contacts) {
        if (contacts != null) {
            return contacts.stream()
                    .map(ContactMapper::mapToContactDTO)
                    .toList();
        }
        return List.of();
    }


    private static List<Contact> mapDtosToContacts(List<ContactDTO> contacts) {
        if (contacts != null) {
            return contacts.stream()
                    .map(ContactMapper::mapToContact)
                    .toList();
        }
        return List.of();
    }
}
