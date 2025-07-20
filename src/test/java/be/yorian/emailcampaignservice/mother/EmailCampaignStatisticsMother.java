package be.yorian.emailcampaignservice.mother;

import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailCampaignStatistics;

import java.time.LocalDateTime;

public class EmailCampaignStatisticsMother {

    public static EmailCampaignStatistics newSavedEmailCampaignStatistics(LocalDateTime createdAt, EmailCampaign emailCampaign) {
        EmailCampaignStatistics emailCampaignStatistics = new EmailCampaignStatistics(emailCampaign);
        emailCampaignStatistics.setId(1L);
        emailCampaignStatistics.setEmailsSent(10L);
        emailCampaignStatistics.setEmailsDelivered(5L);
        emailCampaignStatistics.setEmailsOpened(3L);
        emailCampaignStatistics.setEmailsClicked(2L);
        emailCampaignStatistics.setCreatedAt(createdAt);
        return emailCampaignStatistics;
    }

    public static EmailCampaignStatisticsDTO savedEmailCampaignStatisticsDTO(Long emailCampaignId) {
        return new EmailCampaignStatisticsDTO(
                1L,
                emailCampaignId,
                10,
                5,
                3,
                2
        );
    }

}
