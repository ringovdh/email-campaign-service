package be.yorian.emailcampaignservice.service;

import be.yorian.emailcampaignservice.dto.EmailTemplateDTO;
import be.yorian.emailcampaignservice.model.EmailTemplate;
import be.yorian.emailcampaignservice.repository.EmailTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newEmailTemplateDTO;
import static be.yorian.emailcampaignservice.mother.EmailTemplateMother.newSavedEmailTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.DateUtil.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceImplTest {

    @Mock
    private EmailTemplateRepository repository;

    @InjectMocks
    private EmailTemplateServiceImpl service;

    @Test
    void createEmailTemplate_ShouldCreateEmailTemplate_AndReturnEmailTemplate() {
        Date createdAt = now();
        EmailTemplateDTO newEmailTemplateDTO = newEmailTemplateDTO();
        EmailTemplate savedEmailTemplate = newSavedEmailTemplate(createdAt);

        when(repository.save(any(EmailTemplate.class))).thenReturn(savedEmailTemplate);

        EmailTemplateDTO returnedEmailTemplate = service.createEmailTemplate(newEmailTemplateDTO);

        assertThat(returnedEmailTemplate.name()).isEqualTo(newEmailTemplateDTO.name());
        assertThat(returnedEmailTemplate.subject()).isEqualTo(newEmailTemplateDTO.subject());
        assertThat(returnedEmailTemplate.bodyHtml()).isEqualTo(newEmailTemplateDTO.bodyHtml());
        assertThat(returnedEmailTemplate.createdAt()).isEqualTo(createdAt);
    }

}