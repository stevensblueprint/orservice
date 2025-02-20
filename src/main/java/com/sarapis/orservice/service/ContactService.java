package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import java.util.List;

public interface ContactService {
    List<ContactDTO> getAllContacts();
    ContactDTO getContactById(String id);
    ContactDTO createContact(ContactDTO contactDTO);
    ContactDTO updateContact(String id, ContactDTO contactDTO);
    void deleteContact(String id);
}
