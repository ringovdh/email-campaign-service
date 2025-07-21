package be.yorian.emailcampaignservice.mailchimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class MailchimpConfig {

    private static final Logger log = LoggerFactory.getLogger(MailchimpConfig.class);
    @Value("${mailchimp.api.url}")
    String mailchimpApiUrl;
    @Value("${mailchimp.api.key}")
    String mailchimpApiKey;

    @Bean
    public WebClient mailchimpWebClient() {

        return WebClient.builder()
                .baseUrl(mailchimpApiUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(
                        "basic ", mailchimpApiKey))
                .filter(logRequest())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {}__{}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }
}
