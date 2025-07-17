package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.service.EmailTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/email-templates")
public class EmailTemplateControllerImpl implements EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    public EmailTemplateControllerImpl(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<EmailTemplateDTO> createEmailTemplate(@RequestBody @Valid EmailTemplateDTO emailTemplateDTO) {
        EmailTemplateDTO savedEmailTemplateDTO = emailTemplateService.createEmailTemplate(emailTemplateDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmailTemplateDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(savedEmailTemplateDTO);

    }

}
