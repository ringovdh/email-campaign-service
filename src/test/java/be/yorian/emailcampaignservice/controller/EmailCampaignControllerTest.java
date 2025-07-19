package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.service.EmailCampaignService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newEmailCampaignDTO;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newInvalidEmailCampaignDTO;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.savedEmailCampaignDTO;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailCampaignControllerImpl.class)
class EmailCampaignControllerTest extends BaseControllerTest {

    private static final String EMAIL_CAMPAIGN_BASE_URL = "/email-campaigns";
    private static final String EMAIL_CAMPAIGN_BY_ID_URL = EMAIL_CAMPAIGN_BASE_URL + "/{campaignId}";

    @MockitoBean
    private EmailCampaignService emailCampaignService;


    @Test
    @DisplayName("Create EmailCampaign should return an EmailCampaignDTO")
    void createEmailCampaign_shouldReturnDTO() throws Exception {
        LocalDateTime createdAt = now();
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(createdAt);
        EmailCampaignDTO savedEmailCampaignDTO = savedEmailCampaignDTO(createdAt);

        when(emailCampaignService.createEmailCampaign(any(EmailCampaignDTO.class))).thenReturn(savedEmailCampaignDTO);

        String response = mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaignDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost" + EMAIL_CAMPAIGN_BASE_URL + "/" + savedEmailCampaignDTO.id()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        EmailCampaignDTO responseDto = objectMapper.readValue(response, EmailCampaignDTO.class);

        assertResponse(responseDto, savedEmailCampaignDTO);
    }

    @Test
    @DisplayName("Create EmailCampaign should return an exception when request is not valid")
    void createEmailCampaign_shouldReturnException_whenNotValid() throws Exception {
        EmailCampaignDTO newEmailCampaign = newInvalidEmailCampaignDTO();

        mockMvc.perform(post(EMAIL_CAMPAIGN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailCampaign)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(400)))
                .andExpect(jsonPath("$.validationErrors", hasSize(4)))
                .andExpect(jsonPath("$.validationErrors", containsInAnyOrder(
                        "Please provide a campaign name",
                        "Please provide a templateId",
                        "Please provide at least one contactId",
                        "Please provide a schedule"))
                );
    }

    @Test
    void getEmailCampaignById_shouldReturnEmailCampaignDTO() throws Exception{
        LocalDateTime createdAt = now();
        EmailCampaignDTO savedEmailCampaignDTO = savedEmailCampaignDTO(createdAt);

        when(emailCampaignService.getEmailCampaignById(savedEmailCampaignDTO.id())).thenReturn(savedEmailCampaignDTO);

        String response = mockMvc.perform(get(EMAIL_CAMPAIGN_BY_ID_URL, savedEmailCampaignDTO.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        EmailCampaignDTO responseDto = objectMapper.readValue(response, EmailCampaignDTO.class);

        assertResponse(responseDto, savedEmailCampaignDTO);
    }

    @Test
    @DisplayName("Get EmailCampaign by id should return exception not exists")
    void getEmailCampaignById_shouldReturnException_whenNotExists() throws Exception {
        Long unknownEmailCampaignId = 101L;
        String errorMessage = "EmailCampaign not found with id: " + unknownEmailCampaignId;

        when(emailCampaignService.getEmailCampaignById(unknownEmailCampaignId))
                .thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(get(EMAIL_CAMPAIGN_BY_ID_URL, unknownEmailCampaignId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    private static void assertResponse(EmailCampaignDTO responseDto, EmailCampaignDTO expectedDTO) {
        assertThat(responseDto.id()).isEqualTo(expectedDTO.id());
        assertThat(responseDto.name()).isEqualTo(expectedDTO.name());
        assertThat(responseDto.emailTemplateId()).isEqualTo(expectedDTO.emailTemplateId());
        assertThat(responseDto.contactIds()).isEqualTo(expectedDTO.contactIds());
        assertThat(responseDto.status()).isEqualTo(expectedDTO.status());
        assertThat(responseDto.scheduledAt()).isEqualTo(expectedDTO.scheduledAt());
        assertThat(responseDto.createdAt()).isEqualTo(expectedDTO.createdAt());
    }
}

