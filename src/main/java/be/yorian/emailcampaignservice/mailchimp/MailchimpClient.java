package be.yorian.emailcampaignservice.mailchimp;

import be.yorian.emailcampaignservice.mailchimp.dto.MailchimpEmailTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MailchimpClient {

    private final WebClient webClient;

    @Autowired
    public MailchimpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String createEmailTemplate(MailchimpEmailTemplateDTO emailTemplate) {
        return webClient.post()
                .uri("/templates")
                .bodyValue(emailTemplate)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
