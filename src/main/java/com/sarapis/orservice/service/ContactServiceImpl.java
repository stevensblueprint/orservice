package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.contactRepository = contactRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ContactDTO> getAllContacts() {
        List<ContactDTO> contactDTOs = this.contactRepository.findAll().stream().map(Contact::toDTO).toList();
        contactDTOs.forEach(this::addRelatedData);
        return contactDTOs;
    }

    @Override
    public ContactDTO getContactById(String contactId) {
        Contact contact = this.contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found."));
        ContactDTO contactDTO = contact.toDTO();
        this.addRelatedData(contactDTO);
        return contactDTO;
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = this.contactRepository.save(contactDTO.toEntity(null, null, null, null));
        contactDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(contact.getId(), attributeDTO));
        contactDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(contact.getId(), e));

        Contact createdContact = this.contactRepository.save(contact);
        return this.getContactById(createdContact.getId());
    }

    @Override
    public ContactDTO updateContact(String contactId, ContactDTO contactDTO) {
        Contact contact = this.contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found."));

        contact.setName(contactDTO.getName());
        contact.setTitle(contactDTO.getTitle());
        contact.setDepartment(contactDTO.getDepartment());
        contact.setEmail(contactDTO.getEmail());

        Contact updatedContact = this.contactRepository.save(contact);
        return this.getContactById(updatedContact.getId());
    }

    @Override
    public void deleteContact(String contactId) {
        Contact contact = this.contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found."));
        this.attributeService.deleteRelatedAttributes(contact.getId());
        this.metadataService.deleteRelatedMetadata(contact.getId());
        this.contactRepository.delete(contact);
    }

    private void addRelatedData(ContactDTO contactDTO) {
        contactDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(contactDTO.getId()));
        contactDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(contactDTO.getId()));
    }
}
