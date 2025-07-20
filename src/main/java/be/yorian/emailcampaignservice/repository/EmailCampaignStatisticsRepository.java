package be.yorian.emailcampaignservice.repository;

import be.yorian.emailcampaignservice.model.EmailCampaignStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailCampaignStatisticsRepository extends JpaRepository<EmailCampaignStatistics, Long> {

    Optional<EmailCampaignStatistics> findByEmailCampaignId(Long emailCampaignId);

}
