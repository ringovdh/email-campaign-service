package be.yorian.emailcampaignservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record EmailTemplateDTO(
        Long id,
        @NotBlank(message = "Please provide a template name")
        String name,
        @NotBlank(message = "Please provide a subject")
        String subject,
        @NotBlank(message = "Please provide a bodyHtml")
        String bodyHtml,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
