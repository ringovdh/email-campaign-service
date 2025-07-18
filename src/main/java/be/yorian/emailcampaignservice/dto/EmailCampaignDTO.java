package be.yorian.emailcampaignservice.dto;

import be.yorian.emailcampaignservice.enums.EmailStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record EmailCampaignDTO(
        Long id,
        @NotBlank(message = "Please provide a campaign name")
        String name,
        @NotNull(message = "Please provide a template")
        EmailTemplateDTO emailTemplate,
        @NotEmpty(message = "Please provide at least one contact")
        List<ContactDTO> contacts,
        EmailStatus status,
        @NotNull(message = "Please provide a schedule")
        LocalDateTime scheduledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) { }
