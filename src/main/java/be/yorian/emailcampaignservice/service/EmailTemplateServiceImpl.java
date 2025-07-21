package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.dto.EmailTemplateStatisticsDTO;
import be.yorian.emailcampaignservice.enums.EmailCampaignStatus;
import be.yorian.emailcampaignservice.mailchimp.helper.MailchimpEmailTemplateHelper;
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
    private final MailchimpEmailTemplateHelper mailchimpTemplateHelper;


    @Autowired
    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository,
                                    EmailCampaignRepository emailCampaignRepository,
                                    MailchimpEmailTemplateHelper mailchimpTemplateHelper) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailCampaignRepository = emailCampaignRepository;
        this.mailchimpTemplateHelper = mailchimpTemplateHelper;
    }

    @Override
    public EmailTemplateDTO createEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        EmailTemplate emailTemplate = mapToEmailTemplate(emailTemplateDTO);

        mailchimpTemplateHelper.createTemplateInMailchimp(emailTemplateDTO);
        // TODO add unique id from mailchimp to our emailTemplate
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        log.info("Create EmailTemplate with id: {}", savedEmailTemplate.getId());
        return mapToEmailTemplateDTO(savedEmailTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateDTO getEmailTemplateById(Long emailTemplateId) {
        EmailTemplate template = findEmailTemplateById(emailTemplateId);
        return mapToEmailTemplateDTO(template);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailTemplateDTO> getAllEmailTemplates() {
        return emailTemplateRepository.findAll().stream()
                .map(EmailTemplateMapper::mapToEmailTemplateDTO).toList();
    }

    @Override
    public EmailTemplateDTO updateEmailTemplate(Long emailTemplateId, EmailTemplateDTO updatedEmailTemplateDTO) {
        EmailTemplate emailTemplate = findEmailTemplateById(emailTemplateId);

        updateEmailTemplateFromDTO(emailTemplate, updatedEmailTemplateDTO);
        log.info("Update EmailTemplate with id: {}", emailTemplate.getId());

        return mapToEmailTemplateDTO(emailTemplate);
    }

    @Override
    public void deleteEmailTemplate(Long emailTemplateId) {
        List<Long> linkedEmailCampaignIds = emailCampaignRepository.findAllByTemplateId(emailTemplateId).stream()
                .map(EmailCampaign::getId).toList();
        if (!linkedEmailCampaignIds.isEmpty()) {
            throw new IllegalStateException("Cannot delete emailtemplate that is used by campaign(s): "
                    + linkedEmailCampaignIds );
        }
        emailTemplateRepository.delete(findEmailTemplateById(emailTemplateId));
        log.info("Delete EmailTemplate with id: {}", emailTemplateId);
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
    public EmailTemplateStatisticsDTO getEmailTemplateStatistics(Long emailTemplateId) {
        List<EmailCampaign> emailCampaigns = emailCampaignRepository.findAllByTemplateId(emailTemplateId);
        int numberOfCampaigns = emailCampaigns.size();
        int sentCount = emailCampaigns.stream()
                .filter(c -> c.getStatus().equals(EmailCampaignStatus.SENT))
                .mapToInt(c -> c.getContacts().size()).sum();

        return new EmailTemplateStatisticsDTO(
                emailTemplateId,
                numberOfCampaigns,
                sentCount);
    }

    private EmailTemplate findEmailTemplateById(Long emailTemplateId) {
        return emailTemplateRepository.findById(emailTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(EMAILTEMPLATE_NOT_FOUND + emailTemplateId));
    }
}
