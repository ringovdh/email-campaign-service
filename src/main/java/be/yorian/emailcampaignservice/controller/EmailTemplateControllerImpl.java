package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;
import be.yorian.emailcampaignservice.service.EmailTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;

@RestController
@RequestMapping("/email-templates")
@Tag(name = "Email Template Management", description = "APIs for managing email templates")
public class EmailTemplateControllerImpl implements EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    public EmailTemplateControllerImpl(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    @Operation(summary = "Create a new email template", description = "Create a new email template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email template created successfully",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided",
                    content = @Content(schema = @Schema()))
    })
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
    @Operation(summary = "Get an email template by id", description = "Get an email template based on its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email template found ",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDTO.class))),
            @ApiResponse(responseCode = "404", description = "Email template not found with the given id",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmailTemplateDTO> getEmailTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(emailTemplateService.getEmailTemplateById(id));
    }

    @Override
    @Operation(summary = "Get all email templates", description = "Get a list of all email templates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all templates")
    })
    @GetMapping
    public ResponseEntity<List<EmailTemplateDTO>> getAllEmailTemplates() {
        return ResponseEntity.ok(emailTemplateService.getAllEmailTemplates());
    }

    @Override
    @Operation(summary = "Update an existing email template", description = "Updates the details of an existing email template by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email template updated successfully",
                    content = @Content(schema = @Schema(implementation = EmailTemplateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Email template not found with the given id",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmailTemplateDTO> updateEmailTemplate(
            @PathVariable Long id,
            @RequestBody @Valid EmailTemplateDTO updatedEmailTemplateDTO) {
        return ResponseEntity.ok(emailTemplateService.updateEmailTemplate(id, updatedEmailTemplateDTO));
    }

    @Override
    @Operation(summary = "Delete an email template", description = "Deletes an email template by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email template deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Email template not found with the given id",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmailTemplate(@PathVariable Long id) {
        emailTemplateService.deleteEmailTemplate(id);
    }

    @Override
    @Operation(summary = "Get all updated email templates", description = "Get a list of templates that have been updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of updated templates")
    })
    @GetMapping("/updated")
    public ResponseEntity<List<EmailTemplateDTO>> getUpdatedEmailTemplates() {
        return ResponseEntity.ok(emailTemplateService.getUpdatedEmailTemplates());
    }

    @Override
    @Operation(summary = "Get all unused email templates", description = "Get a list of templates that are not used in an email campaign.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of unused templates")
    })
    @GetMapping("/unused")
    public ResponseEntity<List<EmailTemplateDTO>> getUnusedEmailTemplates() {
        return ResponseEntity.ok(emailTemplateService.getUnusedEmailTemplates());
    }

    @Override
    @Operation(summary = "Get statistics for an email template", description = "Retrieves usage statistics for an email template by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmailTemplateStatisticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Email template not found with the given id",
                    content = @Content)
    })
    @GetMapping("/{id}/statistics")
    public ResponseEntity<EmailTemplateStatisticsDTO> getEmailTemplateStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(emailTemplateService.getEmailTemplateStatistics(id));
    }
}
