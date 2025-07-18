package be.yorian.emailcampaignservice.repository;

import be.yorian.emailcampaignservice.model.EmailCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCampaignRepository extends JpaRepository<EmailCampaign, Long> {

}
