package be.yorian.emailcampaignservice.integration;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newEmailCampaignDTO;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EmailCampaignIT extends BaseIT {

    private static final String EMAIL_CAMPAIGN_BASE_URL = "/email-campaigns";
    private static final String EMAIL_CAMPAIGN_STATISTICS_URL = EMAIL_CAMPAIGN_BASE_URL + "/{campaignId}/statistics";


    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);"""
    )
    @DisplayName("Create an emailCampaign should create save and return emailCampaignDto")
    @Transactional
    void createEmailCampaign_shouldReturnDTO() throws Exception {
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(now(), List.of(1L, 2L));

        String response = mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        EmailCampaignDTO dto = objectMapper.readValue(response, EmailCampaignDTO.class);
        assertThat(dto.id()).isNotEqualTo(0L);
        assertThat(dto.contactIds()).hasSize(newEmailCampaignDTO.contactIds().size());
    }

    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);"""
    )
    @DisplayName("Create an emailCampaign should throw exception when contact not exists")
    @Transactional
    void createEmailCampaign_shouldThrowException_whenContactNotExists() throws Exception {
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(now(),List.of(1L, 2L));

        mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is("Not all contacts were found")));
    }

    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', '2025-07-19T10:00:00', null);"""
    )
    @DisplayName("Create an emailCampaign should throw exception when template not exists")
    @Transactional
    void createEmailCampaign_shouldThrowException_whenTemplateNotExists() throws Exception {
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(now(), List.of(1L, 2L));

        mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is("EmailTemplate not found with id: " + newEmailCampaignDTO.emailTemplateId())));
    }

    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN_STATISTICS(id, email_campaign_id, emails_sent, emails_delivered, emails_opened, emails_clicked, created_at, updated_at)
                    VALUES (1, 1, 10, 5, 3, 1, '2025-07-19T10:00:00', null);"""
    )
    @DisplayName("Get EmailCampaignStatistics should return EmailCampaignStatisticsDTO")
    @Transactional
    void getEmailCampaignStatistics_shouldReturnEmailCampaignStatisticsDTO() throws Exception {
        Long emailCampaignId = 1L;

        String response = mockMvc.perform(get(EMAIL_CAMPAIGN_STATISTICS_URL, emailCampaignId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        EmailCampaignStatisticsDTO responseDto = objectMapper.readValue(response, EmailCampaignStatisticsDTO.class);
        assertThat(responseDto.id()).isEqualTo(emailCampaignId);
        assertThat(responseDto.emailsSent()).isEqualTo(10);

    }
}
