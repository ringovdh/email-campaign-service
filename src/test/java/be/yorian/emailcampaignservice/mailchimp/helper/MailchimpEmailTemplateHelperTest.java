package be.yorian.emailcampaignservice.mailchimp.helper;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.time.LocalDateTime.now;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
class MailchimpEmailTemplateHelperTest {

    @Autowired
    private MailchimpEmailTemplateHelper mailchimpEmailTemplateHelper;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("mailchimp.api.url", () -> "${wiremock.server.baseUrl}");
        registry.add("use.mailchimp.key", () -> "api-key");
        registry.add("use.mailchimp.flag", () -> "true");
    }


    @Test
    @DisplayName("should create template locally and send to Mailchimp when API call is successful")
    void whenMailchimpSucceeds_shouldCreateAndReturnDTO() {
        EmailTemplateDTO templateToCreate = new EmailTemplateDTO(
                1L,
                "Mailchimp mail template",
                "Mailchimp mail subject",
                "Mailchimp mail body",
                now(),
                null
        );

        stubFor(post(urlEqualTo("/templates"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"id\": \"mc123\", \"name\": \"" + templateToCreate.name() + "\"}")));

        mailchimpEmailTemplateHelper.createTemplateInMailchimp(templateToCreate);

        verify(1, postRequestedFor(urlEqualTo("/templates"))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(matchingJsonPath("$.name", equalTo(templateToCreate.name()))));
    }
}