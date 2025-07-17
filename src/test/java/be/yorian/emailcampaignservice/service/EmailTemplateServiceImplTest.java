package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceImplTest {

    private Date createdAt;

    @Mock
    private EmailTemplateRepository repository;

    @InjectMocks
    private EmailTemplateServiceImpl service;

    @BeforeEach
    void setUp() {
        this.createdAt = now();
    }

    @Test
    @DisplayName("Create EmailTemplate should create and return EmailTemplate")
    void createEmailTemplate_ShouldCreateEmailTemplate_AndReturnEmailTemplate() {
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
    void getEmailTemplateById_ShouldReturnEmailTemplate() {
        Long emailTemplateId = 1L;
        EmailTemplate savedEmailTemplate = newSavedEmailTemplate(createdAt);

        when(repository.findById(emailTemplateId)).thenReturn(Optional.of(savedEmailTemplate));

        EmailTemplateDTO returnedEmailTemplate = service.getEmailTemplateById(emailTemplateId);

        assertThat(returnedEmailTemplate.id()).isEqualTo(emailTemplateId);
        assertThat(returnedEmailTemplate.name()).isEqualTo(savedEmailTemplate.getName());
        assertThat(returnedEmailTemplate.subject()).isEqualTo(savedEmailTemplate.getSubject());
        assertThat(returnedEmailTemplate.bodyHtml()).isEqualTo(savedEmailTemplate.getBodyHtml());
        assertThat(returnedEmailTemplate.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Get EmailTemplate by id should return exception when not exists")
    void getEmailTemplateById_ShouldThrowException_WhenNotExists() {
        Long unknownEmailTemplateId = 101L;

        when(repository.findById(unknownEmailTemplateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getEmailTemplateById(unknownEmailTemplateId));
    }

}