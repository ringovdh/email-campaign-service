package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;

import java.util.List;

public interface EmailTemplateService {

    EmailTemplateDTO createEmailTemplate(EmailTemplateDTO emailTemplate);

    EmailTemplateDTO getEmailTemplateById(Long id);

    EmailTemplateDTO updateEmailTemplate(Long id, EmailTemplateDTO emailTemplate);

    void deleteEmailTemplate(Long id);

    List<EmailTemplateDTO> getUpdatedEmailTemplates();

    List<EmailTemplateDTO> getUnusedEmailTemplates();
}
