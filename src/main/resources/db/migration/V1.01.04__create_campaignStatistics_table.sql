CREATE TABLE IF NOT EXISTS email_campaign_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email_campaign_id BIGINT NOT NULL REFERENCES email_campaign(id),
    emails_sent BIGINT,
    emails_delivered BIGINT,
    emails_opened BIGINT,
    emails_clicked BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);