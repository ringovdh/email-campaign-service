package be.yorian.emailcampaignservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record EmailTemplateDTO(
        Long id,
        @NotBlank(message = "Please provide a template name")
        String name,
        @NotBlank(message = "Please provide a subject")
        String subject,
        @NotBlank(message = "Please provide a bodyHtml")
        String bodyHtml,
        Date createdAt,
        Date updatedAt) {
}
