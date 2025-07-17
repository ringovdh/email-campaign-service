package be.yorian.emailcampaignservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EmailCampaignServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailCampaignServiceApplication.class, args);
    }

}
