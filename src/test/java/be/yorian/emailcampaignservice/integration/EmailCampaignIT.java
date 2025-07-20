package be.yorian.emailcampaignservice.integration;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailCampaignStatistics;
import be.yorian.emailcampaignservice.repository.EmailCampaignStatisticsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static be.yorian.emailcampaignservice.enums.EmailCampaignStatus.DRAFT;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newEmailCampaignDTO;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.updatedEmailCampaignDTO;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EmailCampaignIT extends BaseIT {

    private static final String EMAIL_CAMPAIGN_BASE_URL = "/email-campaigns";
    private static final String EMAIL_CAMPAIGN_BY_ID_URL = EMAIL_CAMPAIGN_BASE_URL + "/{campaignId}";
    private static final String EMAIL_CAMPAIGN_STATISTICS_URL = EMAIL_CAMPAIGN_BASE_URL + "/{campaignId}/statistics";

    @Autowired
    private EmailCampaignStatisticsRepository emailCampaignStatisticsRepository;


    @Test
    @Sql(statements = """
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (2, 'Test another email campaign', 1, 'DRAFT', '2025-07-25T10:00:00', '2025-07-20T10:00:00', null);"""
    )
    @DisplayName("Get all emailCampaigns should return a list of emailCampaigns")
    @Transactional
    void getAllEmailCampaigns_shouldReturnListOfDto() throws Exception {
        String response = mockMvc.perform(get(EMAIL_CAMPAIGN_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<EmailCampaignDTO> responseDtos = objectMapper.readValue(response, new TypeReference<>() {});

        assertThat(responseDtos)
                .hasSize(2)
                .extracting(EmailCampaignDTO::id)
                .containsExactlyInAnyOrder(1L, 2L);
    }


    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);"""
    )
    @DisplayName("Create an emailCampaign should create save and return emailCampaignDto and EmailCampaignStatistics")
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
        assertThat(dto.name()).isEqualTo(newEmailCampaignDTO.name());
        assertThat(dto.contactIds()).containsExactlyInAnyOrderElementsOf(newEmailCampaignDTO.contactIds());
        assertThat(dto.emailTemplateId()).isEqualTo(newEmailCampaignDTO.emailTemplateId());
        assertThat(dto.scheduledAt()).isEqualTo(newEmailCampaignDTO.scheduledAt());
        assertThat(dto.status()).isEqualTo(DRAFT);

        Optional<EmailCampaignStatistics> emailCampaignStatistics = emailCampaignStatisticsRepository
                .findByEmailCampaignId(dto.id());

        assertThat(emailCampaignStatistics)
                .isPresent()
                .get()
                .extracting(EmailCampaignStatistics::getEmailCampaign)
                .extracting(EmailCampaign::getId)
                .isEqualTo(dto.id());
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
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO PUBLIC.EMAIL_CAMPAIGN_CONTACT(email_campaign_id, contact_id) 
                    VALUES(1, 1);"""
    )
    @DisplayName("Get EmailCampaignById should return EmailCampaignDTO")
    @Transactional
    void getEmailCampaignById_shouldReturnEmailCampaignDTO() throws Exception {
        Long emailCampaignId = 1L;

        String response = mockMvc.perform(get(EMAIL_CAMPAIGN_BY_ID_URL, emailCampaignId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        EmailCampaignDTO responseDto = objectMapper.readValue(response, EmailCampaignDTO.class);

        assertThat(responseDto.id()).isEqualTo(emailCampaignId);
        assertThat(responseDto.name()).isEqualTo("Test email campaign");
        assertThat(responseDto.emailTemplateId()).isEqualTo(1L);
        assertThat(responseDto.contactIds()).containsExactlyInAnyOrder(1L);
        assertThat(responseDto.status()).isEqualTo(DRAFT);
        assertThat(responseDto.scheduledAt()).hasDayOfMonth(21).hasMonthValue(7).hasYear(2025);
    }

    @Test
    @DisplayName("Get EmailCampaign by id should return exception when not exists")
    @Transactional
    void getEmailCampaignById_shouldReturnException_whenNotExists() throws Exception {
        Long unknownEmailCampaignId = 101L;
        String errorMessage = "EmailCampaign not found with id: " + unknownEmailCampaignId;

        mockMvc.perform(get(EMAIL_CAMPAIGN_BY_ID_URL, unknownEmailCampaignId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    @Sql(statements = """
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);
            INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                    VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', '2025-07-19T10:00:00', null);
            INSERT INTO PUBLIC.EMAIL_CAMPAIGN_CONTACT(email_campaign_id, contact_id) 
                    VALUES(1, 1);"""
    )
    @DisplayName("Update EmailCampaign should return an updated EmailCampaignDTO")
    @Transactional
    void updateEmailCampaign_shouldReturnUpdatedEmailCampaignDTO() throws Exception {
        EmailCampaignDTO updatedEmailCampaignDTO = updatedEmailCampaignDTO(now(), List.of(1L, 2L));

        String response = mockMvc.perform(put(EMAIL_CAMPAIGN_BY_ID_URL, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedEmailCampaignDTO)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

        EmailCampaignDTO responseDto = objectMapper.readValue(response, EmailCampaignDTO.class);

        assertThat(responseDto.name()).isEqualTo(updatedEmailCampaignDTO.name());
        assertThat(responseDto.contactIds()).containsExactlyInAnyOrderElementsOf(updatedEmailCampaignDTO.contactIds());
        assertThat(responseDto.emailTemplateId()).isEqualTo(updatedEmailCampaignDTO.emailTemplateId());
        assertThat(responseDto.scheduledAt()).isEqualTo(updatedEmailCampaignDTO.scheduledAt());
        assertThat(responseDto.status()).isEqualTo(DRAFT);
        assertThat(responseDto.updatedAt()).isAfter(responseDto.createdAt());
    }

    @Test
    @Sql(statements = """
            INSERT INTO EMAIL_TEMPLATE(id, name, subject, body_html, created_at, updated_at)
                                VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', '2025-07-19T10:00:00', null);
                        INSERT INTO EMAIL_CAMPAIGN(id, name, template_id, status, scheduled_at, created_at, updated_at)
                                VALUES (1, 'Test email campaign', 1, 'DRAFT', '2025-07-21T10:00:00', '2025-07-19T10:00:00', null);
                        INSERT INTO CONTACT(id, email, created_at, updated_at)
                                VALUES(1, 'test.a@prompto.com', '2025-07-19T10:00:00', null);
                        INSERT INTO PUBLIC.EMAIL_CAMPAIGN_CONTACT(email_campaign_id, contact_id)\s
                                VALUES(1, 1);"""
    )
    @DisplayName("Delete EmailCampaign should delete EmailCampaign and EmailCampaignStatistics")
    @Transactional
    void deleteEmailCampaign_shouldDeleteEmailCampaignAndEmailCampaignStatistics() throws Exception {
        Long emailCampaignId = 1L;

        mockMvc.perform(delete(EMAIL_CAMPAIGN_BY_ID_URL, emailCampaignId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(EMAIL_CAMPAIGN_BY_ID_URL, emailCampaignId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is("EmailCampaign not found with id: " + emailCampaignId)));

        mockMvc.perform(get(EMAIL_CAMPAIGN_STATISTICS_URL, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is("EmailCampaignStatistics not found with emailCampaignId: 1")));
    }

    @Test
    @Sql(statements = """
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
        assertThat(responseDto.id()).isEqualTo(1L);
        assertThat(responseDto.campaignId()).isEqualTo(emailCampaignId);
        assertThat(responseDto.emailsSent()).isEqualTo(10);
        assertThat(responseDto.emailsDelivered()).isEqualTo(5);
        assertThat(responseDto.emailsOpened()).isEqualTo(3);
        assertThat(responseDto.emailsClicked()).isEqualTo(1);

    }
}
