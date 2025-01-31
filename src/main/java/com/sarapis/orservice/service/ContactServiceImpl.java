package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.upsert.UpsertContactDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final OrganizationRepository organizationRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final LocationRepository locationRepository;
    private final PhoneRepository phoneRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository,
                              OrganizationRepository organizationRepository,
                              ServiceRepository serviceRepository,
                              ServiceAtLocationRepository serviceAtLocationRepository,
                              LocationRepository locationRepository,
                              PhoneRepository phoneRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.contactRepository = contactRepository;
        this.organizationRepository = organizationRepository;
        this.serviceRepository = serviceRepository;
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.locationRepository = locationRepository;
        this.phoneRepository = phoneRepository;
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
    public ContactDTO createContact(UpsertContactDTO upsertContactDTO) {
        Contact createdContact = this.contactRepository.save(upsertContactDTO.create());

        if (upsertContactDTO.getOrganizationId() != null) {
            Organization organization = this.organizationRepository.findById(upsertContactDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
            organization.getContacts().add(createdContact);
            this.organizationRepository.save(organization);
            createdContact.setOrganization(organization);
        }

        if (upsertContactDTO.getServiceId() != null) {
            com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(upsertContactDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
            service.getContacts().add(createdContact);
            this.serviceRepository.save(service);
            createdContact.setService(service);
        }

        if (upsertContactDTO.getServiceAtLocationId() != null) {
            ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(upsertContactDTO.getServiceAtLocationId())
                    .orElseThrow(() -> new RuntimeException("Service at location not found."));
            serviceAtLocation.getContacts().add(createdContact);
            this.serviceAtLocationRepository.save(serviceAtLocation);
            createdContact.setServiceAtLocation(serviceAtLocation);
        }

        if (upsertContactDTO.getLocationId() != null) {
            Location location = this.locationRepository.findById(upsertContactDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found."));
            location.getContacts().add(createdContact);
            this.locationRepository.save(location);
            createdContact.setLocation(location);
        }

        createdContact.setPhones(new ArrayList<>());
        for (String id : upsertContactDTO.getPhones()) {
            Phone phone = this.phoneRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Phone not found."));
            phone.setContact(createdContact);
            this.phoneRepository.save(phone);
            createdContact.getPhones().add(phone);
        }

        this.contactRepository.save(createdContact);
        return this.getContactById(createdContact.getId());
    }

    @Override
    public ContactDTO updateContact(String contactId, UpsertContactDTO upsertContactDTO) {
        Contact contact = this.contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found."));
        Contact updatedContact = upsertContactDTO.merge(contact);
        updatedContact.setId(contactId);

        if (upsertContactDTO.getOrganizationId() != null) {
            Organization organization = this.organizationRepository.findById(upsertContactDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
            organization.getContacts().add(updatedContact);
            this.organizationRepository.save(organization);
            if (contact.getOrganization() != null) {
                contact.getOrganization().getContacts().remove(contact);
            }
            updatedContact.setOrganization(organization);
        }

        if (upsertContactDTO.getServiceId() != null) {
            com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(upsertContactDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
            service.getContacts().add(updatedContact);
            this.serviceRepository.save(service);
            if (contact.getService() != null) {
                contact.getService().getContacts().remove(contact);
            }
            updatedContact.setService(service);
        }

        if (upsertContactDTO.getServiceAtLocationId() != null) {
            ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(upsertContactDTO.getServiceAtLocationId())
                    .orElseThrow(() -> new RuntimeException("Service at location not found."));
            serviceAtLocation.getContacts().add(updatedContact);
            this.serviceAtLocationRepository.save(serviceAtLocation);
            if (contact.getServiceAtLocation() != null) {
                contact.getServiceAtLocation().getContacts().remove(contact);
            }
            updatedContact.setServiceAtLocation(serviceAtLocation);
        }

        if (upsertContactDTO.getLocationId() != null) {
            Location location = this.locationRepository.findById(upsertContactDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found."));
            location.getContacts().add(updatedContact);
            this.locationRepository.save(location);
            if (contact.getLocation() != null) {
                contact.getLocation().getContacts().remove(contact);
            }
            updatedContact.setLocation(location);
        }

        if (upsertContactDTO.getPhones() != null && !upsertContactDTO.getPhones().isEmpty()) {
            for (Phone phone : contact.getPhones()) {
                phone.setContact(null);
                this.phoneRepository.save(phone);
            }
            updatedContact.setPhones(new ArrayList<>());
            for (String id : upsertContactDTO.getPhones()) {
                Phone phone = this.phoneRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Phone not found."));
                phone.setContact(updatedContact);
                this.phoneRepository.save(phone);
                updatedContact.getPhones().add(phone);
            }
        }

        this.contactRepository.save(updatedContact);
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
