package be.yorian.emailcampaignservice.dto;

public record EmailTemplateStatisticsDTO(
        Long templateId,
        long campaigns,
        long emailsSend) { }
