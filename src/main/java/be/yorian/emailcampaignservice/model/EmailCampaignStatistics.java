package be.yorian.emailcampaignservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_campaign_statistics")
public class EmailCampaignStatistics extends BaseClass {

    @OneToOne
    @JoinColumn(name = "email_campaign_id", nullable = false)
    private EmailCampaign emailCampaign;
    private long emailsSent;
    private long emailsDelivered;
    private long emailsOpened;
    private long emailsClicked;

    public EmailCampaignStatistics() { }

    public EmailCampaignStatistics(EmailCampaign emailCampaign) {
        this.emailCampaign = emailCampaign;
    }

    public EmailCampaign getEmailCampaign() {
        return emailCampaign;
    }

    public void setEmailCampaign(EmailCampaign emailCampaign) {
        this.emailCampaign = emailCampaign;
    }

    public long getEmailsSent() {
        return emailsSent;
    }

    public void setEmailsSent(long emailsSent) {
        this.emailsSent = emailsSent;
    }

    public long getEmailsDelivered() {
        return emailsDelivered;
    }

    public void setEmailsDelivered(long emailsDelivered) {
        this.emailsDelivered = emailsDelivered;
    }

    public long getEmailsOpened() {
        return emailsOpened;
    }

    public void setEmailsOpened(long emailsOpened) {
        this.emailsOpened = emailsOpened;
    }

    public long getEmailsClicked() {
        return emailsClicked;
    }

    public void setEmailsClicked(long emailsClicked) {
        this.emailsClicked = emailsClicked;
    }
}
