package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplate;
import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.updateEmailTemplateFromDTO;

@Service
@Transactional
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private static final Logger log = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);
    private static final String EMAILTEMPLATE_NOT_FOUND = "EmailTemplate not found with id: ";
    private final EmailTemplateRepository emailTemplateRepository;

    @Autowired
    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
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

    private EmailTemplate findTemplateById(Long id) {
        return emailTemplateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EMAILTEMPLATE_NOT_FOUND + id));
    }
}
