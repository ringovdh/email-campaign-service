package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.service.EmailCampaignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newEmailCampaignDTO;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.savedEmailCampaignDTO;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailCampaignControllerImpl.class)
class EmailCampaignControllerTest extends BaseControllerTest {

    private static final String EMAIL_CAMPAIGN_BASE_URL = "/email-campaigns";

    @MockitoBean
    private EmailCampaignService emailCampaignService;

    @Test
    @DisplayName("Create EmailCampaign should return an EmailCampaignDTO")
    void createEmailCampaign_shouldReturnDTO() throws Exception {
        LocalDateTime createdAt = now();
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(createdAt);
        EmailCampaignDTO savedEmailCampaignDTO = savedEmailCampaignDTO(createdAt);

        when(emailCampaignService.createEmailCampaign(any(EmailCampaignDTO.class))).thenReturn(savedEmailCampaignDTO);

        mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost" + EMAIL_CAMPAIGN_BASE_URL + "/" + savedEmailCampaignDTO.id()))
                .andExpect(jsonPath("$.id", is(savedEmailCampaignDTO.id().intValue())))
                .andExpect(jsonPath("$.name", is(savedEmailCampaignDTO.name())))
                .andExpect(jsonPath("$.emailTemplateId", is(savedEmailCampaignDTO.emailTemplateId().intValue())))
                .andExpect(jsonPath("$.contactIds", hasSize(2)))
                .andExpect(jsonPath("$.status", is(savedEmailCampaignDTO.status().toString())))
                .andExpect(jsonPath("$.scheduledAt", is(savedEmailCampaignDTO.scheduledAt().toString())))
                .andExpect(jsonPath("$.createdAt", is(createdAt.toString())));
    }

}

