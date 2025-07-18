package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;

public final class EmailTemplateMapper {

    public static EmailTemplate mapToEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        if (emailTemplateDTO != null) {
            EmailTemplate emailTemplate = new EmailTemplate();
            updateEmailTemplateFromDTO(emailTemplate, emailTemplateDTO);
            return emailTemplate;
        }
        return null;
    }

    public static EmailTemplateDTO mapToEmailTemplateDTO(EmailTemplate emailTemplate) {
        if (emailTemplate != null) {
            return new EmailTemplateDTO(
                    emailTemplate.getId(),
                    emailTemplate.getName(),
                    emailTemplate.getSubject(),
                    emailTemplate.getBodyHtml(),
                    emailTemplate.getCreatedAt(),
                    emailTemplate.getUpdatedAt());
        }
        return null;
    }

    public static void updateEmailTemplateFromDTO(EmailTemplate emailTemplate, EmailTemplateDTO updatedEmailTemplateDTO) {
        if (emailTemplate == null || updatedEmailTemplateDTO == null) {
            return;
        }
        emailTemplate.setName(updatedEmailTemplateDTO.name());
        emailTemplate.setSubject(updatedEmailTemplateDTO.subject());
        emailTemplate.setBodyHtml(updatedEmailTemplateDTO.bodyHtml());
    }
}
