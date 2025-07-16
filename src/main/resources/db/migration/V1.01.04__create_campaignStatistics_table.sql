CREATE TABLE IF NOT EXISTS campaign_statistics (
    id BIGINT PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES email_campaign(id),
    emailsSent INT,
    emailsDelivered INT,
    emailsOpened INT,
    emailsClicked INT
);