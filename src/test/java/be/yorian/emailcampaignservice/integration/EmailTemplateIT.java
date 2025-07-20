package be.yorian.emailcampaignservice.integration;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailTemplateIT extends BaseIT {

    private static final String EMAIL_TEMPLATE_BASE_URL = "/email-templates";
    private static final String GET_EMAIL_TEMPLATE_UPDATED_URL = EMAIL_TEMPLATE_BASE_URL + "/updated";
    private static final String GET_EMAIL_TEMPLATE_UNUSED_URL = EMAIL_TEMPLATE_BASE_URL + "/unused";
    private static final String GET_EMAIL_TEMPLATE_STATISTICS_URL = EMAIL_TEMPLATE_BASE_URL + "/{id}/statistics";


    @Test
    @Sql(statements = """
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', '2025-07-19T10:00:00');
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                                VALUES (2, 'Test updated email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', '2025-07-21T10:00:00');"""
    )
    @DisplayName("Get all updated emailTemplates should return all updated emailTemplates")
    @Transactional
    void getAllUpdatedEmailTemplates_shouldReturnListOfDto() throws Exception {

        String response = mockMvc.perform(get(GET_EMAIL_TEMPLATE_UPDATED_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EmailTemplateDTO> responseDtos = objectMapper.readValue(response, new TypeReference<>() {});
        assertThat(responseDtos).isNotNull();
        assertThat(responseDtos).hasSize(1);

        EmailTemplateDTO dto = responseDtos.getFirst();
        assertThat(dto.updatedAt()).isAfter(dto.createdAt());
        assertThat(dto.name()).isEqualTo("Test updated email template");
    }

    @Test
    @Sql(statements = """
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', '2025-07-19T10:00:00');
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (2, 'Test unused email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', '2025-07-19T10:00:00');
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);"""
    )
    @DisplayName("Get all emailTemplates not used in campaigns")
    @Transactional
    void getAllEmailTemplates_notUsedInCampaigns_shouldReturnListOfDto() throws Exception {
        String response = mockMvc.perform(get(GET_EMAIL_TEMPLATE_UNUSED_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EmailTemplateDTO> responseDtos = objectMapper.readValue(response, new TypeReference<>() {});
        assertThat(responseDtos).isNotNull();
        assertThat(responseDtos).hasSize(1);

        EmailTemplateDTO dto = responseDtos.getFirst();
        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEqualTo("Test unused email template");
    }

    @Test
    @Sql(statements = """
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', '2025-07-19T10:00:00');
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (2, 'Test email campaign 2', 1, 'SENT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN_CONTACT(email_campaign_id, contact_id)
                    VALUES (1, 1);
            INSERT INTO EMAIL_CAMPAIGN_CONTACT(email_campaign_id, contact_id)
                    VALUES (2, 1);
            INSERT INTO EMAIL_CAMPAIGN_CONTACT(email_campaign_id, contact_id)
                    VALUES (2, 2);"""
    )
    @DisplayName("Get emailTemplateStatistics for emailTemplate returns emailTemplateStatisticsDto")
    @Transactional
    void getEmailTemplateStatistics_byEmailTemplateId_shouldReturnEmailTemplateStatistics() throws Exception {
        String response = mockMvc.perform(get(GET_EMAIL_TEMPLATE_STATISTICS_URL, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EmailTemplateStatisticsDto responseDto = objectMapper.readValue(response, EmailTemplateStatisticsDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.campaigns()).isEqualTo(2);
        assertThat(responseDto.emailsSend()).isEqualTo(2);
    }
}
