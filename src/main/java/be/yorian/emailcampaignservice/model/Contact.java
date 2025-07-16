package be.yorian.emailcampaignservice.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class Contact extends BaseClass {

    @Email(message = "Please provide a valid email address")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
