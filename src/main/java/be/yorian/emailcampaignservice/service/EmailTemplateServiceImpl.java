package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;
import be.yorian.emailcampaignservice.enums.EmailStatus;
import be.yorian.emailcampaignservice.mapper.EmailTemplateMapper;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailCampaignRepository;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplate;
import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.updateEmailTemplateFromDTO;

@Service
@Transactional
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private static final Logger log = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);
    private static final String EMAILTEMPLATE_NOT_FOUND = "EmailTemplate not found with id: ";
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailCampaignRepository emailCampaignRepository;

    @Autowired
    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository,
                                    EmailCampaignRepository emailCampaignRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailCampaignRepository = emailCampaignRepository;
    }

    @Override
    public EmailTemplateDTO createEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        EmailTemplate emailTemplate = mapToEmailTemplate(emailTemplateDTO);
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        log.info("Create EmailTemplate with id: {}", savedEmailTemplate.getId());

        return mapToEmailTemplateDTO(savedEmailTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateDTO getEmailTemplateById(Long id) {
        EmailTemplate template = findTemplateById(id);
        return mapToEmailTemplateDTO(template);
    }

    @Override
    public EmailTemplateDTO updateEmailTemplate(Long id, EmailTemplateDTO updatedEmailTemplateDTO) {
        EmailTemplate emailTemplate = findTemplateById(id);

        updateEmailTemplateFromDTO(emailTemplate, updatedEmailTemplateDTO);
        log.info("Update EmailTemplate with id: {}", emailTemplate.getId());

        return mapToEmailTemplateDTO(emailTemplate);
    }

    @Override
    public void deleteEmailTemplate(Long id) {
        emailTemplateRepository.delete(findTemplateById(id));
        log.info("Delete EmailTemplate with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailTemplateDTO> getUpdatedEmailTemplates() {
        return emailTemplateRepository.findAllByUpdatedAtIsAfterCreatedAt().stream()
                .map(EmailTemplateMapper::mapToEmailTemplateDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailTemplateDTO> getUnusedEmailTemplates() {
        List<Long> emailTemplateIds = emailCampaignRepository.findAllByTemplateIsNotNull().stream()
                .map(ec -> ec.getTemplate().getId())
                .toList();

        return emailTemplateRepository.findAll().stream()
                .filter(et -> !emailTemplateIds.contains(et.getId()))
                .map(EmailTemplateMapper::mapToEmailTemplateDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateStatisticsDTO getEmailTemplateStatistics(Long id) {
        List<EmailCampaign> emailCampaigns = emailCampaignRepository.findAllByTemplateId(id);
        int numberOfCampaigns = emailCampaigns.size();
        int sentCount = emailCampaigns.stream()
                .filter(c -> c.getStatus().equals(EmailStatus.SENT))
                .mapToInt(c -> c.getContacts().size()).sum();

        return new EmailTemplateStatisticsDTO(
                id,
                numberOfCampaigns,
                sentCount);
    }

    private EmailTemplate findTemplateById(Long id) {
        return emailTemplateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EMAILTEMPLATE_NOT_FOUND + id));
    }
}
