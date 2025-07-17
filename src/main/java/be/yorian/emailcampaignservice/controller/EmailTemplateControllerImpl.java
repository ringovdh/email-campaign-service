package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.service.EmailTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @PostMapping
    public ResponseEntity<EmailTemplateDTO> createEmailTemplate(@RequestBody @Valid EmailTemplateDTO emailTemplateDTO) {
        EmailTemplateDTO savedEmailTemplateDTO = emailTemplateService.createEmailTemplate(emailTemplateDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmailTemplateDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(savedEmailTemplateDTO);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmailTemplateDTO> getEmailTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(emailTemplateService.getEmailTemplateById(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EmailTemplateDTO> updateEmailTemplate(
            @PathVariable Long id,
            @RequestBody @Valid EmailTemplateDTO updatedEmailTemplateDTO) {
        return ResponseEntity.ok(emailTemplateService.updateEmailTemplate(id, updatedEmailTemplateDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmailTemplate(@PathVariable Long id) {
        emailTemplateService.deleteEmailTemplate(id);
    }
}
