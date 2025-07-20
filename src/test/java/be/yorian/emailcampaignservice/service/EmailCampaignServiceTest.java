package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailCampaignStatistics;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.ContactRepository;
import be.yorian.emailcampaignservice.repository.EmailCampaignRepository;
import be.yorian.emailcampaignservice.repository.EmailCampaignStatisticsRepository;
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
import static be.yorian.emailcampaignservice.mother.EmailCampaignStatisticsMother.newSavedEmailCampaignStatistics;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailCampaignServiceTest {

    private LocalDateTime createdAt;
    private Contact contactA;
    private Contact contactB;
    @Mock
    private EmailCampaignRepository emailCampaignRepository;
    @Mock
    private EmailTemplateRepository emailTemplateRepository;
    @Mock
    private EmailCampaignStatisticsRepository emailCampaignStatisticsRepository;
    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private EmailCampaignServiceImpl emailCampaignService;


    @BeforeEach
    void setUp() {
        createdAt = now();
        contactA = savedContactA();
        contactB = savedContactB();
    }

    @Test
    @DisplayName("Create an EmailCampaign should create and return EmailCampaign")
    void createEmailCampaign_shouldCreateEmailCampaign_returnsNewEmailCampaign() {
        List<Contact> contacts = List.of(contactA, contactB);
        List<Long> contactIds = List.of(contactA.getId(), contactB.getId());
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(createdAt, contactIds);
        EmailCampaign savedEmailCampaign = newSavedEmailCampaign(createdAt, contacts);
        savedEmailCampaign.setContacts(contacts);
        EmailTemplate emailTemplate = newSavedEmailTemplate(createdAt);

        when(emailCampaignRepository.save(any(EmailCampaign.class))).thenReturn(savedEmailCampaign);
        when(emailTemplateRepository.findById(newEmailCampaignDTO.emailTemplateId()))
                .thenReturn(Optional.of(emailTemplate));
        when(contactRepository.findAllById(newEmailCampaignDTO.contactIds())).thenReturn(contacts);

        EmailCampaignDTO returnedEmailCampaignDTO = emailCampaignService.createEmailCampaign(newEmailCampaignDTO);

        assertThat(returnedEmailCampaignDTO.name()).isEqualTo(newEmailCampaignDTO.name());
        assertThat(returnedEmailCampaignDTO.emailTemplateId()).isEqualTo(newEmailCampaignDTO.emailTemplateId());
        assertThat(returnedEmailCampaignDTO.contactIds()).hasSize(newEmailCampaignDTO.contactIds().size());
        assertThat(returnedEmailCampaignDTO.scheduledAt()).isEqualTo(newEmailCampaignDTO.scheduledAt());
        assertThat(returnedEmailCampaignDTO.status()).isEqualTo(DRAFT);
        assertThat(returnedEmailCampaignDTO.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Create an EmailCampaign should throw exception when contact not exists")
    void createEmailCampaign_whenContactsNotFound_throwsException() {
        List<Contact> contacts = List.of(contactA);
        List<Long> contactIds = List.of(contactA.getId(), contactB.getId());
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(createdAt, contactIds);
        when(contactRepository.findAllById(newEmailCampaignDTO.contactIds())).thenReturn(contacts);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailCampaignService.createEmailCampaign(newEmailCampaignDTO));

        assertThat(exception.getMessage()).isEqualTo("Not all contacts were found");
    }

    @Test
    @DisplayName("Create an EmailCampaign should throw exception when template not exists")
    void createEmailCampaign_whenTemplateNotFound_throwsException() {
        List<Contact> contacts = List.of(contactA, contactB);
        EmailCampaignDTO newEmailCampaignDTO = newEmailCampaignDTO(createdAt, List.of(1L, 2L));
        when(contactRepository.findAllById(newEmailCampaignDTO.contactIds()))
                .thenReturn(contacts);
        when(emailTemplateRepository.findById(newEmailCampaignDTO.emailTemplateId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailCampaignService.createEmailCampaign(newEmailCampaignDTO));

        assertThat(exception.getMessage())
                .isEqualTo("EmailTemplate not found with id: " + newEmailCampaignDTO.emailTemplateId());
    }

    @Test
    @DisplayName("Get EmailCampaign by id should return EmailCampaignDto")
    void getEmailCampaignById_shouldReturnEmailCampaign() {
        List<Contact> contacts = List.of(contactA);
        EmailCampaign savedEmailCampaign = newSavedEmailCampaign(createdAt, contacts);

        when(emailCampaignRepository.findById(savedEmailCampaign.getId())).thenReturn(Optional.of(savedEmailCampaign));

        EmailCampaignDTO returnedEmailCampaign = emailCampaignService.getEmailCampaignById(savedEmailCampaign.getId());

        assertThat(returnedEmailCampaign.id()).isEqualTo(savedEmailCampaign.getId());
        assertThat(returnedEmailCampaign.name()).isEqualTo(savedEmailCampaign.getName());
        assertThat(returnedEmailCampaign.emailTemplateId()).isEqualTo(savedEmailCampaign.getTemplate().getId());
        assertThat(returnedEmailCampaign.contactIds()).hasSize(savedEmailCampaign.getContacts().size());
        assertThat(returnedEmailCampaign.status()).isEqualTo(savedEmailCampaign.getStatus());
        assertThat(returnedEmailCampaign.scheduledAt()).isEqualTo(savedEmailCampaign.getScheduledAt());
        assertThat(returnedEmailCampaign.createdAt()).isEqualTo(savedEmailCampaign.getCreatedAt());
    }

    @Test
    @DisplayName("Get EmailCampaign by id should return exception when not exists")
    void getEmailCampaignById_shouldThrowException_whenNotExists() {
        Long unknownEmailCampaignId = 101L;

        when(emailCampaignRepository.findById(unknownEmailCampaignId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailCampaignService.getEmailCampaignById(unknownEmailCampaignId));
        assertThat(exception.getMessage())
                .isEqualTo("EmailCampaign not found with id: " + unknownEmailCampaignId);
    }

    @Test
    @DisplayName("Get EmailCampaignStatistics by emailCampaign id should return EmailCampaignStatisticsDto")
    void getEmailCampaignStatistics_byEmailCampaignId_shouldReturnEmailCampaignStatistics() {
        Long emailCampaignId = 1L;
        List<Contact> contacts = List.of(contactA, contactB);
        EmailCampaign emailCampaign = newSavedEmailCampaign(createdAt, contacts);
        EmailCampaignStatistics emailCampaignStatistics = newSavedEmailCampaignStatistics(createdAt, emailCampaign);

        when(emailCampaignStatisticsRepository.findByEmailCampaignId(emailCampaignId))
                .thenReturn(Optional.of(emailCampaignStatistics));

        EmailCampaignStatisticsDTO returnedEmailCampaignStatistics = emailCampaignService.getEmailCampaignStatistics(emailCampaignId);

        assertThat(returnedEmailCampaignStatistics.id()).isEqualTo(emailCampaignStatistics.getId());
        assertThat(returnedEmailCampaignStatistics.campaignId()).isEqualTo(emailCampaignStatistics.getEmailCampaign().getId());
        assertThat(returnedEmailCampaignStatistics.emailsSent()).isEqualTo(emailCampaignStatistics.getEmailsSent());
        assertThat(returnedEmailCampaignStatistics.emailsDelivered()).isEqualTo(emailCampaignStatistics.getEmailsDelivered());
        assertThat(returnedEmailCampaignStatistics.emailsOpened()).isEqualTo(emailCampaignStatistics.getEmailsOpened());
        assertThat(returnedEmailCampaignStatistics.emailsClicked()).isEqualTo(emailCampaignStatistics.getEmailsClicked());
    }

    @Test
    @DisplayName("Get EmailCampaignStatistics by emailCampaign id should return exception when not exists")
    void getEmailCampaign_byEmailCampaignId_shouldThrowException_whenNotExists() {
        Long unknownEmailCampaignId = 101L;

        when(emailCampaignStatisticsRepository.findByEmailCampaignId(unknownEmailCampaignId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> emailCampaignService.getEmailCampaignStatistics(unknownEmailCampaignId));
        assertThat(exception.getMessage())
                .isEqualTo("EmailCampaignStatistics not found with emailCampaignId: " + unknownEmailCampaignId);
    }
}