package be.yorian.emailcampaignservice.repository;

import be.yorian.emailcampaignservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    @Query("select t from EmailTemplate t where t.createdAt < t.updatedAt")
    List<EmailTemplate> findAllByUpdatedAtIsAfterCreatedAt();
}
