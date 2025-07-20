package be.yorian.emailcampaignservice.dto;

public record EmailTemplateStatisticsDto(
        Long templateId,
        int campaigns,
        int emailsSend) { }
