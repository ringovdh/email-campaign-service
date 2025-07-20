package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmailCampaignController {

    ResponseEntity<EmailCampaignDTO> createEmailCampaign(EmailCampaignDTO emailCampaignDTO);

    ResponseEntity<EmailCampaignDTO> getEmailCampaignById(Long id);

    ResponseEntity<List<EmailCampaignDTO>> getAllEmailCampaigns();

    ResponseEntity<EmailCampaignStatisticsDTO> getEmailCampaignStatistics(Long id);
}
