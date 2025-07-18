package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailCampaignDTO;
import be.yorian.emailcampaignservice.model.EmailCampaign;
import be.yorian.emailcampaignservice.repository.EmailCampaignRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static be.yorian.emailcampaignservice.mapper.EmailCampaignMapper.mapToEmailCampaign;
import static be.yorian.emailcampaignservice.mapper.EmailCampaignMapper.mapToEmailCampaignDTO;

@Service
@Transactional
public class EmailCampaignServiceImpl implements EmailCampaignService {

    private final EmailCampaignRepository emailCampaignRepository;

    @Autowired
    public EmailCampaignServiceImpl(EmailCampaignRepository emailCampaignRepository) {
        this.emailCampaignRepository = emailCampaignRepository;
    }

    public EmailCampaignDTO createEmailCampaign(EmailCampaignDTO emailCampaignDTO) {
        EmailCampaign emailCampaign = mapToEmailCampaign(emailCampaignDTO);
        EmailCampaign savedEmailCampaign = emailCampaignRepository.save(emailCampaign);
        return mapToEmailCampaignDTO(savedEmailCampaign);
    }
}
