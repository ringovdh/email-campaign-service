package be.yorian.emailcampaignservice.model;

import be.yorian.emailcampaignservice.enums.EmailStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class EmailCampaign extends  BaseClass {

    @NotBlank(message = "Please provide a campaign name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private EmailTemplate template;

    @ManyToMany
    @JoinTable(name = "email_campaign_contact",
        joinColumns =  @JoinColumn(name = "email_campaign_id"),
        inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private List<Contact> contacts;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @NotNull
    private LocalDateTime scheduledAt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EmailTemplate template) {
        this.template = template;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

}
