package be.yorian.emailcampaignservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class EmailTemplate extends BaseClass {

    @NotBlank(message = "Please provide a template name")
    private String name;

    @NotBlank(message = "Please provide a subject")
    private String subject;

    @NotBlank(message = "Please provide a body")
    private String bodyHtml;

    @OneToMany(mappedBy = "template")
    private List<EmailCampaign> campaigns;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public  List<EmailCampaign> getCampaigns() {
        return campaigns;
    }

}
