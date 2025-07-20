package be.yorian.emailcampaignservice.repository;

import be.yorian.emailcampaignservice.model.EmailCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailCampaignRepository extends JpaRepository<EmailCampaign, Long> {

    @Query("select c from EmailCampaign c where c.template is not null")
    List<EmailCampaign> findAllByTemplateIsNotNull();

    List<EmailCampaign> findAllByTemplateId(Long id);
}
