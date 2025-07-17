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
import static java.time.LocalDateTime.now;

@Service
@Transactional
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private static final Logger log = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);
    private final EmailTemplateRepository emailTemplateRepository;

    @Autowired
    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public EmailTemplateDTO createEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        EmailTemplate emailTemplate = mapToEmailTemplate(emailTemplateDTO);
        emailTemplate.setCreatedAt(now());
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        log.info("Create EmailTemplate with id: {}", savedEmailTemplate.getId());

        return mapToEmailTemplateDTO(savedEmailTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateDTO getEmailTemplateById(Long id) {
        EmailTemplate template = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EmailTemplate not found with id: " + id));
        return mapToEmailTemplateDTO(template);
    }

    @Override
    public EmailTemplateDTO updateEmailTemplate(Long id, EmailTemplateDTO updatedEmailTemplateDTO) {
        EmailTemplate template = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EmailTemplate not found with id: " + id));

        template.setName(updatedEmailTemplateDTO.name());
        template.setSubject(updatedEmailTemplateDTO.subject());
        template.setBodyHtml(updatedEmailTemplateDTO.bodyHtml());
        template.setUpdatedAt(now());

        log.info("Update EmailTemplate with id: {}", template.getId());

        return mapToEmailTemplateDTO(template);
    }

}
