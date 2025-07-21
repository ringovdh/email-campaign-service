package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;

import java.util.List;

public interface EmailTemplateService {

    EmailTemplateDTO createEmailTemplate(EmailTemplateDTO emailTemplate);

    EmailTemplateDTO getEmailTemplateById(Long emailTemplateId);

    List<EmailTemplateDTO> getAllEmailTemplates();

    EmailTemplateDTO updateEmailTemplate(Long emailTemplateId, EmailTemplateDTO emailTemplate);

    void deleteEmailTemplate(Long emailTemplateId);

    List<EmailTemplateDTO> getUpdatedEmailTemplates();

    List<EmailTemplateDTO> getUnusedEmailTemplates();

    EmailTemplateStatisticsDTO getEmailTemplateStatistics(Long emailTemplateId);

}
