package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.upsert.UpsertContactDTO;

import java.util.List;

public interface ContactService {
    List<ContactDTO> getAllContacts();

    ContactDTO getContactById(String contactId);

    ContactDTO createContact(UpsertContactDTO upsertContactDTO);

    ContactDTO updateContact(String contactId, ContactDTO contactDTO);

    void deleteContact(String contactId);
}
