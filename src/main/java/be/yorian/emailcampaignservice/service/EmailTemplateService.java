package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;

public interface EmailTemplateService {

    EmailTemplateDTO createEmailTemplate(EmailTemplateDTO emailTemplate);

}
