package be.yorian.emailcampaignservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "campaign_statistics")
public class CampaignStatistics extends BaseClass {

    private Long campaignId;
    private int emailsSent;
    private int emailsDelivered;
    private int emailsOpened;
    private int emailsClicked;


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public int getEmailsSent() {
        return emailsSent;
    }

    public void setEmailsSent(int emailsSent) {
        this.emailsSent = emailsSent;
    }

    public int getEmailsDelivered() {
        return emailsDelivered;
    }

    public void setEmailsDelivered(int emailsDelivered) {
        this.emailsDelivered = emailsDelivered;
    }

    public int getEmailsOpened() {
        return emailsOpened;
    }

    public void setEmailsOpened(int emailsOpened) {
        this.emailsOpened = emailsOpened;
    }

    public int getEmailsClicked() {
        return emailsClicked;
    }

    public void setEmailsClicked(int emailsClicked) {
        this.emailsClicked = emailsClicked;
    }
}
