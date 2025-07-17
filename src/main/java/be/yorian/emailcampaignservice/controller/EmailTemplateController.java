package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import org.springframework.http.ResponseEntity;


public interface EmailTemplateController {

    ResponseEntity<EmailTemplateDTO> createEmailTemplate(EmailTemplateDTO emailTemplateDTO);
}
