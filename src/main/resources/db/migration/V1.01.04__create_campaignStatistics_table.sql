CREATE TABLE IF NOT EXISTS campaign_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES email_campaign(id),
    emailsSent BIGINT,
    emailsDelivered BIGINT,
    emailsOpened BIGINT,
    emailsClicked BIGINT
);