package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mapper.EmailTemplateMapper.mapToEmailTemplate;

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
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        log.info("Create EmailTemplate with id: {}", savedEmailTemplate.getId());

        return mapToEmailTemplateDTO(savedEmailTemplate);
    }


}
