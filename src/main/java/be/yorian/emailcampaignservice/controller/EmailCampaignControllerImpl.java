package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.service.EmailCampaignService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmailCampaignDTO> getEmailCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(emailCampaignService.getEmailCampaignById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EmailCampaignDTO>> getAllEmailCampaigns() {
        return ResponseEntity.ok(emailCampaignService.getAllEmailCampaigns());
    }

    @Override
    @GetMapping("/{id}/statistics")
    public ResponseEntity<EmailCampaignStatisticsDTO> getEmailCampaignStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(emailCampaignService.getEmailCampaignStatistics(id));
    }

}
