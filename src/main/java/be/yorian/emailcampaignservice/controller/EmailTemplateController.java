package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface EmailTemplateController {

    ResponseEntity<EmailTemplateDTO> createEmailTemplate(EmailTemplateDTO emailTemplateDTO);

    ResponseEntity<EmailTemplateDTO> getEmailTemplateById(Long emailTemplateId);

    ResponseEntity<List<EmailTemplateDTO>> getAllEmailTemplates();

    ResponseEntity<EmailTemplateDTO> updateEmailTemplate(Long emailTemplateId, EmailTemplateDTO emailTemplateDTO);

    void deleteEmailTemplate(Long emailTemplateId);

    ResponseEntity<List<EmailTemplateDTO>> getUpdatedEmailTemplates();

    ResponseEntity<List<EmailTemplateDTO>> getUnusedEmailTemplates();

    ResponseEntity<EmailTemplateStatisticsDTO> getEmailTemplateStatistics(Long emailTemplateId);

}
