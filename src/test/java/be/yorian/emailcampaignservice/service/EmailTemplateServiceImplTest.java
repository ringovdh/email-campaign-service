package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDto;
import be.yorian.emailcampaignservice.enums.EmailStatus;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailCampaignRepository;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newSavedEmailCampaign;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.updatedEmailTemplate;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.updatedEmailTemplateDTO;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceImplTest {

    private LocalDateTime createdAt;
    private EmailTemplate savedEmailTemplate;
    @Mock
    private EmailTemplateRepository emailTemplateRepository;
    @Mock
    private EmailCampaignRepository emailCampaignRepository;
    @InjectMocks
    private EmailTemplateServiceImpl emailTemplateService;

    @BeforeEach
    void setUp() {
        createdAt = now();
        savedEmailTemplate = newSavedEmailTemplate(createdAt);
    }

    @Test
    @DisplayName("Create EmailTemplate should create and return EmailTemplate")
    void createEmailTemplate_shouldCreateEmailTemplate_returnsNewEmailTemplate() {
        EmailTemplateDTO newEmailTemplateDTO = newEmailTemplateDTO();

        when(emailTemplateRepository.save(any(EmailTemplate.class))).thenReturn(savedEmailTemplate);

        EmailTemplateDTO returnedEmailTemplate = emailTemplateService.createEmailTemplate(newEmailTemplateDTO);

        assertThat(returnedEmailTemplate.name()).isEqualTo(newEmailTemplateDTO.name());
        assertThat(returnedEmailTemplate.subject()).isEqualTo(newEmailTemplateDTO.subject());
        assertThat(returnedEmailTemplate.bodyHtml()).isEqualTo(newEmailTemplateDTO.bodyHtml());
        assertThat(returnedEmailTemplate.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Get EmailTemplate by id should return EmailTemplate")
    void getEmailTemplateById_shouldReturnEmailTemplate() {
        when(emailTemplateRepository.findById(savedEmailTemplate.getId())).thenReturn(Optional.of(savedEmailTemplate));

        EmailTemplateDTO returnedEmailTemplate = emailTemplateService.getEmailTemplateById(savedEmailTemplate.getId());

        assertThat(returnedEmailTemplate.id()).isEqualTo(savedEmailTemplate.getId());
        assertThat(returnedEmailTemplate.name()).isEqualTo(savedEmailTemplate.getName());
        assertThat(returnedEmailTemplate.subject()).isEqualTo(savedEmailTemplate.getSubject());
        assertThat(returnedEmailTemplate.bodyHtml()).isEqualTo(savedEmailTemplate.getBodyHtml());
        assertThat(returnedEmailTemplate.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Get EmailTemplate by id should return exception when not exists")
    void getEmailTemplateById_shouldThrowException_whenNotExists() {
        Long unknownEmailTemplateId = 101L;

        when(emailTemplateRepository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailTemplateService.getEmailTemplateById(unknownEmailTemplateId));

        assertThat(exception.getMessage())
                .isEqualTo("EmailTemplate not found with id: " + unknownEmailTemplateId);
    }

    @Test
    @DisplayName("Update EmailTemplate should update and return updated EmailTemplate")
    void updateEmailTemplate_shouldUpdateEmailTemplate_returnUpdatedEmailTemplate() {
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);

        when(emailTemplateRepository.findById(savedEmailTemplate.getId())).thenReturn(Optional.of(savedEmailTemplate));

        EmailTemplateDTO returnedEmailTemplate = emailTemplateService.updateEmailTemplate(
                savedEmailTemplate.getId(), updatedEmailTemplateDTO);

        assertThat(returnedEmailTemplate.name()).isEqualTo(updatedEmailTemplateDTO.name());
        assertThat(returnedEmailTemplate.subject()).isEqualTo(updatedEmailTemplateDTO.subject());
        assertThat(returnedEmailTemplate.bodyHtml()).isEqualTo(updatedEmailTemplateDTO.bodyHtml());
        assertThat(returnedEmailTemplate.createdAt()).isEqualTo(createdAt);
        assertThat(returnedEmailTemplate.updatedAt()).isAfter(createdAt);
    }

    @Test
    @DisplayName("Update EmailTemplate should return exception when not exists")
    void updateEmailTemplate_shouldThrowException_whenNotExists() {
        Long unknownEmailTemplateId = 101L;
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);

        when(emailTemplateRepository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailTemplateService.updateEmailTemplate(unknownEmailTemplateId, updatedEmailTemplateDTO));

        assertThat(exception.getMessage())
                .isEqualTo("EmailTemplate not found with id: " + unknownEmailTemplateId);
    }

    @Test
    @DisplayName("Delete EmailTemplate should delete EmailTemplate")
    void deleteEmailTemplate_shouldDeleteEmailTemplate() {
        when(emailTemplateRepository.findById(savedEmailTemplate.getId())).thenReturn(Optional.of(savedEmailTemplate));

        emailTemplateService.deleteEmailTemplate(savedEmailTemplate.getId());

        verify(emailTemplateRepository, times(1)).delete(savedEmailTemplate);
    }

    @Test
    @DisplayName("Delete EmailTemplate should return exception when not exists")
    void deleteEmailTemplate_shouldThrowException_whenNotExists() {
        Long unknownEmailTemplateId = 101L;

        when(emailTemplateRepository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailTemplateService.deleteEmailTemplate(unknownEmailTemplateId));

        assertThat(exception.getMessage())
                .isEqualTo("EmailTemplate not found with id: " + unknownEmailTemplateId);
    }

    @Test
    @DisplayName("Get updated templates should return all updated templates")
    void getUpdatedTemplates_shouldReturnAllUpdatedEmailTemplates() {
        LocalDateTime updatedAt = now().plusDays(1);
        EmailTemplate updatedEmailTemplate = updatedEmailTemplate(createdAt, updatedAt);

        when(emailTemplateRepository.findAllByUpdatedAtIsAfterCreatedAt()).thenReturn(List.of(updatedEmailTemplate));

        List<EmailTemplateDTO> returnedDtos = emailTemplateService.getUpdatedEmailTemplates();

        assertThat(returnedDtos).isNotNull();
        assertThat(returnedDtos).hasSize(1);

        EmailTemplateDTO dto = returnedDtos.getFirst();
        assertThat(dto.id()).isEqualTo(updatedEmailTemplate.getId());
        assertThat(dto.updatedAt()).isAfter(dto.createdAt());
    }

    @Test
    @DisplayName("Get unused templates should return all unused templates")
    void getUnusedTemplates_shouldReturnAllUnusedEmailTemplates() {
        EmailCampaign emailCampaign = newSavedEmailCampaign(createdAt);
        EmailTemplate usedEmailTemplate = newSavedEmailTemplate(createdAt);
        EmailTemplate unusedEmailTemplate = newSavedEmailTemplate(createdAt);
        unusedEmailTemplate.setId(5L);

        when(emailCampaignRepository.findAllByTemplateIsNotNull()).thenReturn(List.of(emailCampaign));
        when(emailTemplateRepository.findAll()).thenReturn(List.of(usedEmailTemplate, unusedEmailTemplate));

        List<EmailTemplateDTO> returnedDtos = emailTemplateService.getUnusedEmailTemplates();

        assertThat(returnedDtos).isNotNull();
        assertThat(returnedDtos).hasSize(1);

        EmailTemplateDTO dto = returnedDtos.getFirst();
        assertThat(dto.id()).isEqualTo(unusedEmailTemplate.getId());
    }

    @Test
    @DisplayName("Get emailTemplateStatistics returns emailTemplateStatistics")
    void getEmailTemplateStatistics_shouldReturnCorrectEmailTemplateStatistics() {
        Long templateId = 1L;
        EmailCampaign draftCampaign = newSavedEmailCampaign(createdAt);
        EmailCampaign sentCampaign = newSavedEmailCampaign(createdAt);
        sentCampaign.setId(3L);
        sentCampaign.setStatus(EmailStatus.SENT);

        when(emailCampaignRepository.findAllByTemplateId(templateId)).thenReturn(List.of(draftCampaign, sentCampaign));
        EmailTemplateStatisticsDto statistics = emailTemplateService.getEmailTemplateStatistics(templateId);

        assertThat(statistics.templateId()).isEqualTo(templateId);
        assertThat(statistics.campaigns()).isEqualTo(2);
        assertThat(statistics.emailsSend()).isEqualTo(sentCampaign.getContacts().size());
    }

}