package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
            .map(Contact::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ContactDTO getContactById(String id) {
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        return contact.toDTO();
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        // Generate an ID if none is provided
        if (contactDTO.getId() == null || contactDTO.getId().isEmpty()) {
            contactDTO.setId(UUID.randomUUID().toString());
        }
        Contact contact = mapToEntity(contactDTO);
        Contact savedContact = contactRepository.save(contact);
        return savedContact.toDTO();
    }

    @Override
    public ContactDTO updateContact(String id, ContactDTO contactDTO) {
        Contact existingContact = contactRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        // Update basic fields
        existingContact.setName(contactDTO.getName());
        existingContact.setTitle(contactDTO.getTitle());
        existingContact.setDepartment(contactDTO.getDepartment());
        existingContact.setEmail(contactDTO.getEmail());
        // For relations (organization, service, etc.) add update logic if required

        Contact updatedContact = contactRepository.save(existingContact);
        return updatedContact.toDTO();
    }

    @Override
    public void deleteContact(String id) {
        Contact existingContact = contactRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        contactRepository.delete(existingContact);
    }

    // Helper method to map a ContactDTO to a Contact entity.
    // This simple version only maps basic fields.
    private Contact mapToEntity(ContactDTO contactDTO) {
        return Contact.builder()
            .id(contactDTO.getId())
            .name(contactDTO.getName())
            .title(contactDTO.getTitle())
            .department(contactDTO.getDepartment())
            .email(contactDTO.getEmail())
            // Relations such as organization, service, etc. could be set here if needed.
            .build();
    }
}
