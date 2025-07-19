package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;

public interface EmailCampaignService {

    EmailCampaignDTO createEmailCampaign(EmailCampaignDTO emailCampaignDTO);

    EmailCampaignDTO getEmailCampaignById(Long id);
}
