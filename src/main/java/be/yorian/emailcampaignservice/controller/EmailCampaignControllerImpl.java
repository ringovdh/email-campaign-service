package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.service.EmailCampaignService;
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
@RequestMapping("/email-campaigns")
@Tag(name = "Email Campaign Management", description = "APIs for managing email campaigns")

public class EmailCampaignControllerImpl implements EmailCampaignController {

    private final EmailCampaignService emailCampaignService;

    public EmailCampaignControllerImpl(EmailCampaignService emailCampaignService) {
        this.emailCampaignService = emailCampaignService;
    }


    @Override
    @Operation(summary = "Create a new email campaign", description = "Create a new email campaign")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email campaign created successfully",
                    content = @Content(schema = @Schema(implementation = EmailCampaignDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "A specified Contact or EmailTemplate was not found",
                    content = @Content)
    })
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
    @Operation(summary = "Get an email campaign by id", description = "Get an email campaign by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email campaign found",
                    content = @Content(schema = @Schema(implementation = EmailCampaignDTO.class))),
            @ApiResponse(responseCode = "404", description = "Email campaign not found with the given ID",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmailCampaignDTO> getEmailCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(emailCampaignService.getEmailCampaignById(id));
    }

    @Override
    @Operation(summary = "Get all email campaigns", description = "Get a list of all email campaigns.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all campaigns")
    })
    @GetMapping
    public ResponseEntity<List<EmailCampaignDTO>> getAllEmailCampaigns() {
        return ResponseEntity.ok(emailCampaignService.getAllEmailCampaigns());
    }

    @Override
    @Operation(summary = "Update an existing email campaign", description = "Updates the details of an existing email campaign by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email campaign updated successfully",
                    content = @Content(schema = @Schema(implementation = EmailCampaignDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Email campaign, Contact, or EmailTemplate not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmailCampaignDTO> updateEmailCampaign(
            @PathVariable Long id,
            @RequestBody @Valid EmailCampaignDTO updatedEmailCampaignDTO) {
        return ResponseEntity.ok(emailCampaignService.updateEmailCampaign(id, updatedEmailCampaignDTO));
    }

    @Override
    @Operation(summary = "Delete an email campaign", description = "Deletes an email campaign and its associated statistics by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email campaign deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Email campaign not found with the given id",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmailCampaign(@PathVariable Long id) {
        emailCampaignService.deleteEmailCampaign(id);
    }

    @Override
    @Operation(summary = "Get statistics for an email campaign", description = "Retrieves performance statistics for an email campaign by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = EmailCampaignStatisticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Statistics not found for the given campaign id",
                    content = @Content)
    })
    @GetMapping("/{id}/statistics")
    public ResponseEntity<EmailCampaignStatisticsDTO> getEmailCampaignStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(emailCampaignService.getEmailCampaignStatistics(id));
    }

}
