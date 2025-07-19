package be.yorian.emailcampaignservice.integration;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newEmailCampaignDTO;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class EmailCampaignIT {

    private static final String EMAIL_CAMPAIGN_BASE_URL = "/email-campaigns";
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', now(), null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', now(), null);
            INSERT INTO email_template(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', now(), null);"""
    )
    @DisplayName("Create an emailCampaign should create save and return emailCampaignDto")
    @Transactional
    public void createEmailCampaign_shouldReturnDTO() throws Exception {
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(now());

        String result =  mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        EmailCampaignDTO dto = objectMapper.readValue(result, EmailCampaignDTO.class);
        assertNotEquals(0, dto.id());
    }

    @Test
    @Sql(statements = """
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(1, 'test.a@prompto.com', now(), null);
            INSERT INTO email_template(id, name, subject, body_html, created_at, updated_at)
                    VALUES (1, 'Test email template', 'test subject', 'Hello Prompto', now(), null);"""
    )
    @DisplayName("Create an emailCampaign should throw exception when contact not exists")
    @Transactional
    public void createEmailCampaign_shouldThrowException_whenContactNotExists() throws Exception {
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(now());

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
                    VALUES(1, 'test.a@prompto.com', now(), null);
            INSERT INTO CONTACT(id, email, created_at, updated_at)
                    VALUES(2, 'test.b@prompto.com', now(), null);"""
    )
    @DisplayName("Create an emailCampaign should throw exception when template not exists")
    @Transactional
    public void createEmailCampaign_shouldThrowException_whenTemplateNotExists() throws Exception {
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(now());

        mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is("EmailTemplate not found with id: " + newEmailCampaignDTO.emailTemplateId().intValue())));
    }
}
