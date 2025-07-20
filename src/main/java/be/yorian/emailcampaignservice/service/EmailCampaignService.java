package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;

import java.util.List;

public interface EmailCampaignService {

    EmailCampaignDTO createEmailCampaign(EmailCampaignDTO emailCampaignDTO);

    EmailCampaignDTO getEmailCampaignById(Long id);

    List<EmailCampaignDTO> getAllEmailCampaigns();

    EmailCampaignDTO updateEmailCampaign(Long id, EmailCampaignDTO updatedEmailCampaignDTO);

    void deleteEmailCampaign(Long id);

    EmailCampaignStatisticsDTO getEmailCampaignStatistics(Long id);

}
