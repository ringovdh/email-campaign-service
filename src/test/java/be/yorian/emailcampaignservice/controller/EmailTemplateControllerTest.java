package be.yorian.emailcampaignservice.controller;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;
import be.yorian.emailcampaignservice.service.EmailTemplateService;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.invalidUpdatedEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newInvalidEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.savedEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.updatedEmailTemplateDTO;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailTemplateControllerImpl.class)
class EmailTemplateControllerTest extends BaseControllerTest {

    private static final String EMAIL_TEMPLATE_BASE_URL = "/email-templates";
    private static final String EMAIL_TEMPLATE_BY_ID_URL = EMAIL_TEMPLATE_BASE_URL + "/{templateId}";
    private static final String GET_EMAIL_TEMPLATE_UPDATED_URL = EMAIL_TEMPLATE_BASE_URL + "/updated";
    private static final String GET_EMAIL_TEMPLATE_UNUSED_URL = EMAIL_TEMPLATE_BASE_URL + "/unused";
    private static final String GET_EMAIL_TEMPLATE_STATISTICS_URL = EMAIL_TEMPLATE_BASE_URL + "/{id}/statistics";


    private LocalDateTime createdAt;
    private EmailTemplateDTO savedEmailTemplateDTO;

    @MockitoBean
    private EmailTemplateService emailTemplateService;

    @BeforeEach
    void setUp() {
        createdAt = now();
        savedEmailTemplateDTO = savedEmailTemplateDTO(createdAt);
    }

    @Test
    @DisplayName("Create EmailTemplate should return an EmailTemplateDTO")
    void createEmailTemplate_shouldReturnDTO() throws Exception {
        EmailTemplateDTO newEmailTemplateDTO = newEmailTemplateDTO();

        when(emailTemplateService.createEmailTemplate(any(EmailTemplateDTO.class))).thenReturn(savedEmailTemplateDTO);

        String response = mockMvc.perform(post(EMAIL_TEMPLATE_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmailTemplateDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost" + EMAIL_TEMPLATE_BASE_URL + "/" + savedEmailTemplateDTO.id()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        EmailTemplateDTO responseDto = objectMapper.readValue(response, EmailTemplateDTO.class);
        assertResponse(responseDto, savedEmailTemplateDTO);
    }

    @Test
    @DisplayName("Create EmailTemplate should return an exception when request is not valid")
    void createEmailTemplate_shouldReturnException_whenNotValid() throws Exception {
        EmailTemplateDTO newEmailTemplate = newInvalidEmailTemplateDTO();

        mockMvc.perform(post(EMAIL_TEMPLATE_BASE_URL)
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

    @Test
    @DisplayName("Get EmailTemplate by id should return an EmailTemplateDTO")
    void getEmailTemplateById_shouldReturnEmailTemplateDTO() throws Exception {
        when(emailTemplateService.getEmailTemplateById(savedEmailTemplateDTO.id())).thenReturn(savedEmailTemplateDTO);

        String response = mockMvc.perform(get(EMAIL_TEMPLATE_BY_ID_URL, savedEmailTemplateDTO.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EmailTemplateDTO responseDto = objectMapper.readValue(response, EmailTemplateDTO.class);
        assertResponse(responseDto, savedEmailTemplateDTO);
    }

    @Test
    @DisplayName("Get EmailTemplate by id should return exception when not exists")
    void getEmailTemplateById_shouldReturnException_whenNotExists() throws Exception {
        Long unknownEmailTemplateId = 101L;
        String errorMessage = "EmailTemplate not found with id: " + unknownEmailTemplateId;

        when(emailTemplateService.getEmailTemplateById(unknownEmailTemplateId))
                .thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(get(EMAIL_TEMPLATE_BY_ID_URL, unknownEmailTemplateId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    @DisplayName("Update EmailTemplate should return an updated EmailTemplateDTO")
    void updateEmailTemplate_shouldReturnUpdatedEmailTemplateDTO() throws Exception {
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);

        when(emailTemplateService.updateEmailTemplate(
                eq(savedEmailTemplateDTO.id()),
                any(EmailTemplateDTO.class))).thenReturn(updatedEmailTemplateDTO);

        String response = mockMvc.perform(put(EMAIL_TEMPLATE_BY_ID_URL, savedEmailTemplateDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmailTemplateDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EmailTemplateDTO responseDto = objectMapper.readValue(response, EmailTemplateDTO.class);

        assertResponse(responseDto, updatedEmailTemplateDTO);
    }

    @Test
    @DisplayName("Update EmailTemplate should return exception when not exists")
    void updateEmailTemplate_shouldReturnException_whenNotExists() throws Exception {
        Long unknownEmailTemplateId = 101L;
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);
        String errorMessage = "EmailTemplate not found with id: " + unknownEmailTemplateId;

        when(emailTemplateService.updateEmailTemplate(
                eq(unknownEmailTemplateId),
                any(EmailTemplateDTO.class))).thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(put(EMAIL_TEMPLATE_BY_ID_URL, unknownEmailTemplateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmailTemplateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
        }

    @Test
    @DisplayName("Update EmailTemplate should return exception when request is not valid")
    void updateEmailTemplate_shouldReturnException_whenNotValid() throws Exception {
        Long unknownEmailTemplateId = 101L;
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplateDTO invalidUpdatedEmailTemplateDTO = invalidUpdatedEmailTemplateDTO(createdAt, updatedAt);

        mockMvc.perform(put(EMAIL_TEMPLATE_BY_ID_URL, unknownEmailTemplateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdatedEmailTemplateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(400)))
                .andExpect(jsonPath("$.validationErrors", hasSize(2)))
                .andExpect(jsonPath("$.validationErrors", containsInAnyOrder(
                        "Please provide a template name",
                        "Please provide a subject"))
                );
    }

    @Test
    @DisplayName("Delete EmailTemplate should return no content when EmailTemplate is deleted")
    void deleteEmailTemplate_shouldReturnNoContent_whenEmailTemplateDeleted() throws Exception {
        Long emailTemplateId = 1L;
        doNothing().when(emailTemplateService).deleteEmailTemplate(emailTemplateId);

        mockMvc.perform(delete(EMAIL_TEMPLATE_BY_ID_URL, emailTemplateId))
                .andExpect(status().isNoContent());

        verify(emailTemplateService, times(1))
                .deleteEmailTemplate(emailTemplateId);
    }

    @Test
    @DisplayName("Delete EmailTemplate should return exception when not exists")
    void deleteEmailTemplate_shouldReturnException_whenNotExists() throws Exception {
        Long unknownEmailTemplateId = 101L;
        String errorMessage = "EmailTemplate not found with id: " + unknownEmailTemplateId;

        doThrow(new EntityNotFoundException(errorMessage))
                .when(emailTemplateService).deleteEmailTemplate(unknownEmailTemplateId);

        mockMvc.perform(delete(EMAIL_TEMPLATE_BY_ID_URL, unknownEmailTemplateId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(404)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    @DisplayName("Get updated templates should return all updated templates")
    void getUpdatedTemplates_shouldReturnAllUpdatedEmailTemplates() throws Exception {
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);
        when(emailTemplateService.getUpdatedEmailTemplates()).thenReturn(List.of(updatedEmailTemplateDTO));

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
        assertResponse(dto, updatedEmailTemplateDTO);
    }

    @Test
    @DisplayName("Get templates not used in campaigns should return all unused templates")
    void getTemplates_notUsedInCampaigns_shouldReturnUnusedTemplates() throws Exception {
        when(emailTemplateService.getUnusedEmailTemplates()).thenReturn(List.of(savedEmailTemplateDTO));

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
        assertResponse(dto, savedEmailTemplateDTO);
    }

    @Test
    @DisplayName("Get emailTemplateStatistics for emailTemplate returns emailTemplateStatisticsDto")
    void getEmailTemplateStatistics_byEmailTemplateId_shouldReturnEmailTemplateStatistics() throws Exception {
        when(emailTemplateService.getEmailTemplateStatistics(1L)).thenReturn(new EmailTemplateStatisticsDTO(1L, 2, 2));

        String response = mockMvc.perform(get(GET_EMAIL_TEMPLATE_STATISTICS_URL, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EmailTemplateStatisticsDTO responseDto = objectMapper.readValue(response, EmailTemplateStatisticsDTO.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.campaigns()).isEqualTo(2);
        assertThat(responseDto.emailsSend()).isEqualTo(2);
    }

    private static void assertResponse(EmailTemplateDTO responseDto, EmailTemplateDTO expectedDTO) {
        assertThat(responseDto.id()).isEqualTo(expectedDTO.id());
        assertThat(responseDto.name()).isEqualTo(expectedDTO.name());
        assertThat(responseDto.subject()).isEqualTo(expectedDTO.subject());
        assertThat(responseDto.bodyHtml()).isEqualTo(expectedDTO.bodyHtml());
        assertThat(responseDto.createdAt()).isEqualTo(expectedDTO.createdAt());
        assertThat(responseDto.updatedAt()).isEqualTo(expectedDTO.updatedAt());
    }

}