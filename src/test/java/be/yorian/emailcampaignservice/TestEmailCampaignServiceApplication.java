package be.yorian.emailcampaignservice;

import org.springframework.boot.SpringApplication;

public class TestEmailCampaignServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(EmailCampaignServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
