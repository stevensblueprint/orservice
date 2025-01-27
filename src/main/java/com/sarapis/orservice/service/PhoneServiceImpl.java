package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.entity.Contact;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final LocationRepository locationRepository;
    private final ServiceRepository serviceRepository;
    private final OrganizationRepository organizationRepository;
    private final ContactRepository contactRepository;
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository,
                            LocationRepository locationRepository,
                            ServiceRepository serviceRepository,
                            OrganizationRepository organizationRepository,
                            ContactRepository contactRepository,
                            ServiceAtLocationRepository serviceAtLocationRepository,
                            AttributeService attributeService,
                            MetadataService metadataService) {
        this.phoneRepository = phoneRepository;
        this.locationRepository = locationRepository;
        this.serviceRepository = serviceRepository;
        this.organizationRepository = organizationRepository;
        this.contactRepository = contactRepository;
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<PhoneDTO> getAllPhones() {
        List<PhoneDTO> phoneDTOs = this.phoneRepository.findAll()
                .stream().map(Phone::toDTO).toList();
        phoneDTOs.forEach(this::addRelatedData);
        return phoneDTOs;
    }

    @Override
    public PhoneDTO getPhoneById(String phoneId) {
        Phone phone = this.phoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found."));
        PhoneDTO phoneDTO = phone.toDTO();
        this.addRelatedData(phoneDTO);
        return phoneDTO;
    }

    @Override
    public PhoneDTO createPhone(PhoneDTO phoneDTO) {
        Location location = null;
        com.sarapis.orservice.entity.core.Service service = null;
        Organization organization = null;
        Contact contact = null;
        ServiceAtLocation serviceAtLocation = null;

        if (phoneDTO.getLocationId() != null) {
            location = this.locationRepository.findById(phoneDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found."));
        }
        if (phoneDTO.getServiceId() != null) {
            service = this.serviceRepository.findById(phoneDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
        }
        if (phoneDTO.getOrganizationId() != null) {
            organization = this.organizationRepository.findById(phoneDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
        }
        if (phoneDTO.getContactId() != null) {
            contact = this.contactRepository.findById(phoneDTO.getContactId())
                    .orElseThrow(() -> new RuntimeException("Contact not found."));
        }
        if (phoneDTO.getServiceAtLocationId() != null) {
            serviceAtLocation = this.serviceAtLocationRepository.findById(phoneDTO.getServiceAtLocationId())
                    .orElseThrow(() -> new RuntimeException("Service at location not found."));
        }

        Phone phone = this.phoneRepository.save(phoneDTO.toEntity(location, service, organization, contact, serviceAtLocation));
        phoneDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(phone.getId(), attributeDTO));
        phoneDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(phone.getId(), e));

        Phone createdPhone = this.phoneRepository.save(phone);
        return this.getPhoneById(createdPhone.getId());
    }

    @Override
    public PhoneDTO updatePhone(String phoneId, PhoneDTO phoneDTO) {
        Phone oldPhone = this.phoneRepository.findById(phoneId).orElseThrow(() -> new RuntimeException("Phone not found."));

        oldPhone.setNumber(phoneDTO.getNumber());
        oldPhone.setExtension(phoneDTO.getExtension());
        oldPhone.setType(phoneDTO.getType());
        oldPhone.setDescription(phoneDTO.getDescription());

        Phone updatedPhone = this.phoneRepository.save(oldPhone);
        return this.getPhoneById(updatedPhone.getId());
    }

    @Override
    public void deletePhone(String phoneId) {
        Phone phone = this.phoneRepository.findById(phoneId).orElseThrow(() -> new RuntimeException("Phone not found."));
        this.attributeService.deleteRelatedAttributes(phone.getId());
        this.metadataService.deleteRelatedMetadata(phone.getId());
        this.phoneRepository.delete(phone);
    }

    private void addRelatedData(PhoneDTO phoneDTO) {
        phoneDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(phoneDTO.getId()));
        phoneDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(phoneDTO.getId()));
    }
}
