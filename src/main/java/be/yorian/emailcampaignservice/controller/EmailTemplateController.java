package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface EmailTemplateController {

    ResponseEntity<EmailTemplateDTO> createEmailTemplate(EmailTemplateDTO emailTemplateDTO);

    ResponseEntity<EmailTemplateDTO> getEmailTemplateById(Long id);

    ResponseEntity<EmailTemplateDTO> updateEmailTemplate(Long id, EmailTemplateDTO emailTemplateDTO);

    void deleteEmailTemplate(Long id);

    ResponseEntity<List<EmailTemplateDTO>> getUpdatedEmailTemplates();

    ResponseEntity<List<EmailTemplateDTO>> getUnusedEmailTemplates();

    ResponseEntity<EmailTemplateStatisticsDTO> getEmailTemplateStatistics(Long id);

}
