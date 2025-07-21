package be.yorian.emailcampaignservice.mailchimp.dto;

public record MailchimpEmailTemplateDTO(
        String name,
        String subject,
        String code) { }
