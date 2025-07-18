package be.yorian.emailcampaignservice.mapper;

import be.yorian.emailcampaignservice.dto.ContactDTO;
import be.yorian.emailcampaignservice.model.Contact;

public final class ContactMapper {

    public static Contact mapToContact(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setId(contactDTO.id());
        return contact;
    }

    public static ContactDTO mapToContactDTO(Contact contact) {
        return new ContactDTO(
                contact.getId());
    }
}
