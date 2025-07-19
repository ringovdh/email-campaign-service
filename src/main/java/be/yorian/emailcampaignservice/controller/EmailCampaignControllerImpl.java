package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.service.EmailCampaignService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/email-campaigns")
public class EmailCampaignControllerImpl implements EmailCampaignController {

    private final EmailCampaignService emailCampaignService;

    public EmailCampaignControllerImpl(EmailCampaignService emailCampaignService) {
        this.emailCampaignService = emailCampaignService;
    }


    @Override
    @PostMapping
    public ResponseEntity<EmailCampaignDTO> createEmailCampaign(@RequestBody @Valid EmailCampaignDTO emailCampaignDTO) {
        EmailCampaignDTO savedEmailCampaignDTO = emailCampaignService.createEmailCampaign(emailCampaignDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmailCampaignDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(savedEmailCampaignDTO);
    }

}
