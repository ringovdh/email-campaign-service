package be.yorian.emailcampaignservice.dto;

public record EmailCampaignStatisticsDTO(
        Long id,
        Long campaignId,
        long emailsSent,
        long emailsDelivered,
        long emailsOpened,
        long emailsClicked) { }
