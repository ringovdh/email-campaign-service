package be.yorian.emailcampaignservice.mother;

import be.yorian.emailcampaignservice.model.Contact;

public class ContactMother {

    public static Contact savedContactA() {
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setEmail("test.a@prompto.com");
        return contact;
    }

    public static Contact savedContactB() {
        Contact contact = new Contact();
        contact.setId(2L);
        contact.setEmail("test.b@prompto.com");
        return contact;
    }
}
