package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.dto.EmailCampaignStatisticsDTO;
import be.yorian.emailcampaignservice.mapper.EmailCampaignMapper;
import be.yorian.emailcampaignservice.model.Contact;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailCampaignStatistics;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.ContactRepository;
import be.yorian.emailcampaignservice.repository.EmailCampaignRepository;
import be.yorian.emailcampaignservice.repository.EmailCampaignStatisticsRepository;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static be.yorian.emailcampaignservice.enums.EmailCampaignStatus.DRAFT;
import static be.yorian.emailcampaignservice.mapper.EmailCampaignMapper.mapToEmailCampaign;
import static be.yorian.emailcampaignservice.mapper.EmailCampaignMapper.mapToEmailCampaignDTO;
import static be.yorian.emailcampaignservice.mapper.EmailCampaignMapper.updateEmailCampaignFromDTO;
import static be.yorian.emailcampaignservice.mapper.EmailCampaignStatisticsMapper.mapToEmailCampaignStatisticsDTO;

@Service
@Transactional
public class EmailCampaignServiceImpl implements EmailCampaignService {

    private static final Logger log = LoggerFactory.getLogger(EmailCampaignServiceImpl.class);
    private static final String EMAILCAMPAIGN_NOT_FOUND = "EmailCampaign not found with id: ";
    private final EmailCampaignRepository emailCampaignRepository;
    private final ContactRepository contactRepository;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailCampaignStatisticsRepository emailCampaignStatisticsRepository;


    @Autowired
    public EmailCampaignServiceImpl(EmailCampaignRepository emailCampaignRepository,
                                    ContactRepository contactRepository,
                                    EmailTemplateRepository emailTemplateRepository,
                                    EmailCampaignStatisticsRepository emailCampaignStatisticsRepository) {
        this.emailCampaignRepository = emailCampaignRepository;
        this.contactRepository = contactRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailCampaignStatisticsRepository = emailCampaignStatisticsRepository;
    }

    @Override
    public EmailCampaignDTO createEmailCampaign(EmailCampaignDTO emailCampaignDTO) {
        EmailCampaign emailCampaign = mapToEmailCampaign(emailCampaignDTO);
        emailCampaign.setContacts(handleContacts(emailCampaignDTO.contactIds()));
        emailCampaign.setTemplate(handleEmailTemplate(emailCampaignDTO.emailTemplateId()));
        emailCampaign.setStatus(DRAFT);
        EmailCampaign savedEmailCampaign = emailCampaignRepository.save(emailCampaign);
        createEmailCampaignStatistics(savedEmailCampaign);

        log.info("Create EmailCampaign with id: {}", savedEmailCampaign.getId());

        return mapToEmailCampaignDTO(savedEmailCampaign);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailCampaignDTO getEmailCampaignById(Long id) {
        EmailCampaign emailCampaign = findEmailCampaignById(id);
        return mapToEmailCampaignDTO(emailCampaign);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailCampaignDTO> getAllEmailCampaigns() {
        return emailCampaignRepository.findAll().stream()
                .map(EmailCampaignMapper::mapToEmailCampaignDTO).toList();
    }

    @Override
    public EmailCampaignDTO updateEmailCampaign(Long id, EmailCampaignDTO updatedEmailCampaignDto) {
        EmailCampaign emailCampaign = findEmailCampaignById(id);
        updateEmailCampaignFromDTO(emailCampaign, updatedEmailCampaignDto);
        emailCampaign.setContacts(handleContacts(updatedEmailCampaignDto.contactIds()));
        emailCampaign.setTemplate(handleEmailTemplate(updatedEmailCampaignDto.emailTemplateId()));
        log.info("Update EmailCampaign with id: {}", emailCampaign.getId());
        return mapToEmailCampaignDTO(emailCampaign);
    }

    @Override
    public void deleteEmailCampaign(Long id) {
        EmailCampaign emailCampaign = findEmailCampaignById(id);
        deleteEmailCampaignStatistics(id);

        emailCampaignRepository.delete(emailCampaign);
        log.info("Delete EmailCampaign with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailCampaignStatisticsDTO getEmailCampaignStatistics(Long id) {
        EmailCampaignStatistics emailCampaignStatistics = emailCampaignStatisticsRepository.findByEmailCampaignId(id)
                .orElseThrow(() -> new EntityNotFoundException("EmailCampaignStatistics not found with emailCampaignId: " + id));
        return mapToEmailCampaignStatisticsDTO(emailCampaignStatistics);
    }


    private EmailTemplate handleEmailTemplate(Long emailTemplateId) {
        return emailTemplateRepository.findById(emailTemplateId).orElseThrow(() ->
                new EntityNotFoundException("EmailTemplate not found with id: " + emailTemplateId));
    }

    private List<Contact> handleContacts(List<Long> contactIds) {
        List<Contact> contacts = contactRepository.findAllById(contactIds);
        if (contactIds.size() != contacts.size()) {
            throw new EntityNotFoundException("Not all contacts were found");
        }
        return contacts;
    }

    private EmailCampaign findEmailCampaignById(Long id) {
        return emailCampaignRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(EMAILCAMPAIGN_NOT_FOUND + id));
    }

    private void deleteEmailCampaignStatistics(Long id) {
        emailCampaignStatisticsRepository.findByEmailCampaignId(id)
                .ifPresent(emailCampaignStatisticsRepository::delete);
        log.info("Delete EmailCampaignStatistics with id: {}", id);
    }

    private void createEmailCampaignStatistics(EmailCampaign savedEmailCampaign) {
        EmailCampaignStatistics emailCampaignStatistics = new EmailCampaignStatistics(savedEmailCampaign);
        emailCampaignStatisticsRepository.save(emailCampaignStatistics);
        log.info("Create EmailCampaignStatistics with id: {}", emailCampaignStatistics.getId());
    }
}
