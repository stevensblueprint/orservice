package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final LocationRepository locationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository,
                              OrganizationRepository organizationRepository,
                              ServiceRepository serviceRepository,
                              ServiceAtLocationRepository serviceAtLocationRepository,
                              LocationRepository locationRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.contactRepository = contactRepository;
        this.organizationRepository = organizationRepository;
        this.serviceRepository = serviceRepository;
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.locationRepository = locationRepository;
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
        Organization organization = null;
        com.sarapis.orservice.entity.core.Service service = null;
        ServiceAtLocation serviceAtLocation = null;
        Location location = null;

        if (contactDTO.getOrganizationId() != null) {
            organization = this.organizationRepository.findById(contactDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
        }
        if (contactDTO.getServiceId() != null) {
            service = this.serviceRepository.findById(contactDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
        }
        if (contactDTO.getServiceAtLocationId() != null) {
            serviceAtLocation = this.serviceAtLocationRepository.findById(contactDTO.getServiceAtLocationId())
                    .orElseThrow(() -> new RuntimeException("Service at location not found."));
        }
        if (contactDTO.getLocationId() != null) {
            location = this.locationRepository.findById(contactDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found."));
        }

        Contact contact = this.contactRepository.save(contactDTO.toEntity(organization, service, serviceAtLocation, location));
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
