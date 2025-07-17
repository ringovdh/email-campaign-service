package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;

public class EmailTemplateMapper {

    public static EmailTemplate mapToEmailTemplate(EmailTemplateDTO dto) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setName(dto.name());
        emailTemplate.setSubject(dto.subject());
        emailTemplate.setBodyHtml(dto.bodyHtml());
        return emailTemplate;
    }

    public static EmailTemplateDTO mapToEmailTemplateDTO(EmailTemplate emailTemplate) {
        return new EmailTemplateDTO(
                emailTemplate.getId(),
                emailTemplate.getName(),
                emailTemplate.getSubject(),
                emailTemplate.getBodyHtml(),
                emailTemplate.getCreatedAt(),
                emailTemplate.getUpdatedAt());
    }
}
