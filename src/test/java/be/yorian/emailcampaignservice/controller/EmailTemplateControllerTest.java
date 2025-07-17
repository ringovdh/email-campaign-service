package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.service.EmailTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newInvalidEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.savedEmailTemplateDTO;
import static org.assertj.core.util.DateUtil.now;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailTemplateControllerImpl.class)
class EmailTemplateControllerTest {

    private static final String EMAIL_TEMPLATE_BASE_URL = "/email-templates";
    private static final String CREATE_EMAIL_TEMPLATE_URL = EMAIL_TEMPLATE_BASE_URL + "/";

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    private EmailTemplateService emailTemplateService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create EmailTemplate should return an EmailTemplateDTO")
    void createEmailTemplate_shouldReturnDTO() throws Exception {
        Date createdAt = now();
        EmailTemplateDTO newEmailTemplateDTO = newEmailTemplateDTO();
        EmailTemplateDTO savedEmailTemplateDTO = savedEmailTemplateDTO(createdAt);

        when(emailTemplateService.createEmailTemplate(any(EmailTemplateDTO.class))).thenReturn(savedEmailTemplateDTO);

        mockMvc.perform(post(CREATE_EMAIL_TEMPLATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailTemplateDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/email-templates/5"))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.subject", is("This is the subject")))
                .andExpect(jsonPath("$.bodyHtml", is("Email <b>content</b> comes here.")));
    }

    @Test
    @DisplayName("Create EmailTemplate should return an exception when request is not valid")
    void createEmailTemplate_shouldReturnException_whenNotValid() throws Exception {
        EmailTemplateDTO newEmailTemplate = newInvalidEmailTemplateDTO();

        mockMvc.perform(post(CREATE_EMAIL_TEMPLATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmailTemplate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(400)))
                .andExpect(jsonPath("$.validationErrors", hasSize(2)))
                .andExpect(jsonPath("$.validationErrors", containsInAnyOrder(
                        "Please provide a template name",
                        "Please provide a subject"))
                );
    }

}