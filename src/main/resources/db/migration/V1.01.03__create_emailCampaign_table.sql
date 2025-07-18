CREATE TABLE IF NOT EXISTS email_campaign (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    template_id BIGINT NOT NULL REFERENCES email_template(id),
    status VARCHAR(10),
    scheduled_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);