package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
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

    @Mock
    private EmailTemplateRepository repository;

    @InjectMocks
    private EmailTemplateServiceImpl service;

    @Test
    @DisplayName("Create EmailTemplate should create and return EmailTemplate")
    void createEmailTemplate_shouldCreateEmailTemplate_returnsNewEmailTemplate() {
        LocalDateTime createdAt = now();
        EmailTemplateDTO newEmailTemplateDTO = newEmailTemplateDTO();
        EmailTemplate savedEmailTemplate = newSavedEmailTemplate(createdAt);

        when(repository.save(any(EmailTemplate.class))).thenReturn(savedEmailTemplate);

        EmailTemplateDTO returnedEmailTemplate = service.createEmailTemplate(newEmailTemplateDTO);

        assertThat(returnedEmailTemplate.name()).isEqualTo(newEmailTemplateDTO.name());
        assertThat(returnedEmailTemplate.subject()).isEqualTo(newEmailTemplateDTO.subject());
        assertThat(returnedEmailTemplate.bodyHtml()).isEqualTo(newEmailTemplateDTO.bodyHtml());
        assertThat(returnedEmailTemplate.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Get EmailTemplate by id should return EmailTemplate")
    void getEmailTemplateById_shouldReturnEmailTemplate() {
        LocalDateTime createdAt = now();
        EmailTemplate savedEmailTemplate = newSavedEmailTemplate(createdAt);

        when(repository.findById(savedEmailTemplate.getId())).thenReturn(Optional.of(savedEmailTemplate));

        EmailTemplateDTO returnedEmailTemplate = service.getEmailTemplateById(savedEmailTemplate.getId());

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

        when(repository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getEmailTemplateById(unknownEmailTemplateId));
    }

    @Test
    @DisplayName("Update EmailTemplate should update and return updated EmailTemplate")
    void updateEmailTemplate_shouldUpdateEmailTemplate_returnUpdatedEmailTemplate() {
        LocalDateTime createdAt = now().minusDays(1);
        LocalDateTime updatedAt = now();
        EmailTemplate savedEmailTemplate = newSavedEmailTemplate(createdAt);
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);

        when(repository.findById(savedEmailTemplate.getId())).thenReturn(Optional.of(savedEmailTemplate));

        EmailTemplateDTO returnedEmailTemplate = service.updateEmailTemplate(savedEmailTemplate.getId(), updatedEmailTemplateDTO);

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
        LocalDateTime createdAt = now().minusDays(1);
        LocalDateTime updatedAt = now();
        EmailTemplateDTO updatedEmailTemplateDTO = updatedEmailTemplateDTO(createdAt, updatedAt);

        when(repository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateEmailTemplate(unknownEmailTemplateId, updatedEmailTemplateDTO));
    }

    @Test
    @DisplayName("Delete EmailTemplate should delete EmailTemplate")
    void deleteEmailTemplate_shouldReturnNoContent_whenEmailTemplateDeleted() {
        LocalDateTime createdAt = now();
        EmailTemplate savedEmailTemplate = newSavedEmailTemplate(createdAt);

        when(repository.findById(savedEmailTemplate.getId())).thenReturn(Optional.of(savedEmailTemplate));

        service.deleteEmailTemplate(savedEmailTemplate.getId());

        verify(repository, times(1)).delete(savedEmailTemplate);
    }

    @Test
    @DisplayName("Delete EmailTemplate should return exception when not exists")
    void deleteEmailTemplate_shouldThrowException_whenNotExists() {
        Long unknownEmailTemplateId = 101L;

        when(repository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteEmailTemplate(unknownEmailTemplateId));
    }

}