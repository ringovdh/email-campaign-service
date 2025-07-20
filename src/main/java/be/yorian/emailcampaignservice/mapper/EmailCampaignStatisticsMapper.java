package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.model.EmailCampaignStatistics;

public class EmailCampaignStatisticsMapper {

    public static EmailCampaignStatisticsDTO mapToEmailCampaignStatisticsDTO(EmailCampaignStatistics emailCampaignStatistics) {
        if (emailCampaignStatistics != null) {
            return new EmailCampaignStatisticsDTO(
                    emailCampaignStatistics.getId(),
                    emailCampaignStatistics.getEmailCampaign().getId(),
                    emailCampaignStatistics.getEmailsSent(),
                    emailCampaignStatistics.getEmailsDelivered(),
                    emailCampaignStatistics.getEmailsOpened(),
                    emailCampaignStatistics.getEmailsClicked());
        }
        return null;
    }

}
