package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.ContactRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.contactRepository = contactRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<ContactDTO> getAllContacts() {
        List<ContactDTO> contactDTOs = this.contactRepository.findAll().stream().map(Contact::toDTO).toList();
        contactDTOs.forEach(this::addRelatedData);
        return contactDTOs;
    }

    @Override
    public ContactDTO getContactById(String id) {
        Contact contact = this.contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found."));
        ContactDTO contactDTO = contact.toDTO();
        this.addRelatedData(contactDTO);
        return contactDTO;
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = this.contactRepository.save(contactDTO.toEntity());

        for (AttributeDTO attributeDTO : contactDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(contact.getId()));
        }

        for (MetadataDTO metadataDTO : contactDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(contact.getId()));
        }

        ContactDTO savedContactDTO = this.contactRepository.save(contact).toDTO();
        this.addRelatedData(savedContactDTO);
        return savedContactDTO;
    }

    @Override
    public ContactDTO updateContact(String id, ContactDTO contactDTO) {
        Contact oldContact = this.contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found."));

        oldContact.setName(contactDTO.getName());
        oldContact.setTitle(contactDTO.getTitle());
        oldContact.setDepartment(contactDTO.getDepartment());
        oldContact.setEmail(contactDTO.getEmail());

        Contact updatedContactDTO = this.contactRepository.save(oldContact);
        return updatedContactDTO.toDTO();
    }

    @Override
    public void deleteContact(String id) {
        Contact contact = this.contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found."));
        this.contactRepository.deleteAttributes(contact.getId());
        this.contactRepository.deleteMetadata(contact.getId());
        this.contactRepository.delete(contact);
    }

    private void addRelatedData(ContactDTO contactDTO) {
        contactDTO.getAttributes().addAll(this.contactRepository.getAttributes(contactDTO.getId()).stream().map(Attribute::toDTO).toList());
        contactDTO.getMetadata().addAll(this.contactRepository.getMetadata(contactDTO.getId()).stream().map(Metadata::toDTO).toList());
    }
}
