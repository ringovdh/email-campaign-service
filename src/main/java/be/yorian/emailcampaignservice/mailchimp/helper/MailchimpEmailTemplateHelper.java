package be.yorian.emailcampaignservice.mailchimp.helper;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.mailchimp.MailchimpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToMailchimpEmailTemplateDTO;

@Component
public class MailchimpEmailTemplateHelper {

    private static final Logger log = LoggerFactory.getLogger(MailchimpEmailTemplateHelper.class);
    private final MailchimpClient mailchimpClient;
    private final boolean useMailchimpFlag;

    public MailchimpEmailTemplateHelper(MailchimpClient mailchimpClient,
                                        @Value("${use.mailchimp.flag}") boolean useMailchimpFlag) {
        this.mailchimpClient = mailchimpClient;
        this.useMailchimpFlag = useMailchimpFlag;
    }

    public void createTemplateInMailchimp(EmailTemplateDTO emailTemplateDTO) {
        if (useMailchimpFlag) {
            try {
                String mailchimpResponse = mailchimpClient.createEmailTemplate(
                        mapToMailchimpEmailTemplateDTO(emailTemplateDTO));
                log.info("Successfully created template in Mailchimp. Response: {}", mailchimpResponse);
            } catch (Exception e) {
                log.error("Failed to create template in Mailchimp for template name: {}", emailTemplateDTO.name(), e);
            }
        }
    }
}
