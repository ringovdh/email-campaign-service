package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.ContactRepository;
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

import static be.yorian.emailcampaignservice.enums.EmailStatus.DRAFT;
import static be.yorian.emailcampaignservice.mother.ContactMother.savedContactA;
import static be.yorian.emailcampaignservice.mother.ContactMother.savedContactB;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newEmailCampaignDTO;
import static be.yorian.emailcampaignservice.mother.EmailCampaignMother.newSavedEmailCampaign;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailCampaignServiceTest {

    private LocalDateTime createdAt;
    private EmailCampaignDTO newEmailCampaignDTO;
    @Mock
    private EmailCampaignRepository emailCampaignRepository;
    @Mock
    private EmailTemplateRepository emailTemplateRepository;
    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private EmailCampaignServiceImpl emailCampaignService;


    @BeforeEach
    void setUp() {
        createdAt = now();
        newEmailCampaignDTO = newEmailCampaignDTO(createdAt);
    }

    @Test
    @DisplayName("Create an EmailCampaign should create and return EmailCampaign")
    void createEmailCampaign_shouldCreateEmailCampaign_returnsNewEmailCampaign() {
        EmailCampaign savedEmailCampaign = newSavedEmailCampaign(createdAt);
        EmailTemplate emailTemplate = newSavedEmailTemplate(createdAt);
        Contact contact1 = savedContactA();
        Contact contact2 = savedContactB();

        when(emailCampaignRepository.save(any(EmailCampaign.class))).thenReturn(savedEmailCampaign);
        when(emailTemplateRepository.findById(newEmailCampaignDTO.emailTemplateId())).thenReturn(Optional.of(emailTemplate));
        when(contactRepository.findAllById(newEmailCampaignDTO.contactIds())).thenReturn(List.of(contact1, contact2));

        EmailCampaignDTO returnedEmailCampaignDTO = emailCampaignService.createEmailCampaign(newEmailCampaignDTO);

        assertThat(returnedEmailCampaignDTO.name()).isEqualTo(newEmailCampaignDTO.name());
        assertThat(returnedEmailCampaignDTO.emailTemplateId()).isEqualTo(newEmailCampaignDTO.emailTemplateId());
        assertThat(returnedEmailCampaignDTO.contactIds()).isEqualTo(List.of(contact1.getId(), contact2.getId()));
        assertThat(returnedEmailCampaignDTO.scheduledAt()).isEqualTo(newEmailCampaignDTO.scheduledAt());
        assertThat(returnedEmailCampaignDTO.status()).isEqualTo(DRAFT);
        assertThat(returnedEmailCampaignDTO.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Create an EmailCampaign should throw exception when contact not exists")
    void createEmailCampaign_whenContactsNotFound_throwsException() {
        when(contactRepository.findAllById(newEmailCampaignDTO.contactIds())).thenReturn(List.of(savedContactA()));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailCampaignService.createEmailCampaign(newEmailCampaignDTO));

        assertThat(exception.getMessage()).isEqualTo("Not all contacts were found");
    }

    @Test
    @DisplayName("Create an EmailCampaign should throw exception when template not exists")
    void createEmailCampaign_whenTemplateNotFound_throwsException() {
        when(contactRepository.findAllById(newEmailCampaignDTO.contactIds()))
                .thenReturn(List.of(savedContactA(), savedContactB()));
        when(emailTemplateRepository.findById(newEmailCampaignDTO.emailTemplateId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailCampaignService.createEmailCampaign(newEmailCampaignDTO));

        assertThat(exception.getMessage())
                .isEqualTo("EmailTemplate not found with id: " + newEmailCampaignDTO.emailTemplateId().intValue());
    }

    @Test
    @DisplayName("Get EmailCampaign by id should return EmailCampaignDto")
    void getEmailCampaignById_shouldReturnEmailCampaign() {
        EmailCampaign savedEmailCampaign = newSavedEmailCampaign(createdAt);

        when(emailCampaignRepository.findById(savedEmailCampaign.getId())).thenReturn(Optional.of(savedEmailCampaign));

        EmailCampaignDTO returnedEmailCampaign = emailCampaignService.getEmailCampaignById(savedEmailCampaign.getId());

        assertThat(returnedEmailCampaign.id()).isEqualTo(savedEmailCampaign.getId());
        assertThat(returnedEmailCampaign.name()).isEqualTo(savedEmailCampaign.getName());
        assertThat(returnedEmailCampaign.emailTemplateId()).isEqualTo(savedEmailCampaign.getTemplate().getId());
        assertThat(returnedEmailCampaign.contactIds()).isEqualTo(List.of(savedEmailCampaign.getContacts().get(0).getId(), savedEmailCampaign.getContacts().get(1).getId()));
        assertThat(returnedEmailCampaign.status()).isEqualTo(savedEmailCampaign.getStatus());
        assertThat(returnedEmailCampaign.scheduledAt()).isEqualTo(savedEmailCampaign.getScheduledAt());
        assertThat(returnedEmailCampaign.createdAt()).isEqualTo(savedEmailCampaign.getCreatedAt());
    }

    @Test
    @DisplayName("Get EmailCampaign by id should return exception when not exists")
    void getEmailCampaignById_shouldThrowException_whenNotExists() {
        Long unknownEmailCampaignId = 101L;

        when(emailCampaignRepository.findById(unknownEmailCampaignId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> emailCampaignService.getEmailCampaignById(unknownEmailCampaignId));
        assertThat(exception.getMessage())
                .isEqualTo("EmailCampaign not found with id: " + unknownEmailCampaignId.intValue());
    }
}