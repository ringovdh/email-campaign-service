CREATE TABLE email_campaign_contact (
    email_campaign_id BIGINT NOT NULL,
    contact_id BIGINT NOT NULL,
    PRIMARY KEY (email_campaign_id, contact_id),
    FOREIGN KEY (email_campaign_id) REFERENCES email_campaign(id),
    FOREIGN KEY (contact_id) REFERENCES contact(id)
);