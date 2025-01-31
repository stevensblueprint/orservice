package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertLocationDTO;
import com.sarapis.orservice.entity.*;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final OrganizationRepository organizationRepository;
    private final LanguageRepository languageRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final ContactRepository contactRepository;
    private final AccessibilityRepository accessibilityRepository;
    private final ScheduleRepository scheduleRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository,
                               OrganizationRepository organizationRepository,
                               LanguageRepository languageRepository,
                               AddressRepository addressRepository,
                               PhoneRepository phoneRepository,
                               ContactRepository contactRepository,
                               AccessibilityRepository accessibilityRepository,
                               ScheduleRepository scheduleRepository,
                               AttributeService attributeService,
                               MetadataService metadataService) {
        this.locationRepository = locationRepository;
        this.organizationRepository = organizationRepository;
        this.languageRepository = languageRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.contactRepository = contactRepository;
        this.accessibilityRepository = accessibilityRepository;
        this.scheduleRepository = scheduleRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        List<LocationDTO> locationDTOs = this.locationRepository.findAll()
                .stream().map(Location::toDTO).toList();
        locationDTOs.forEach(this::addRelatedData);
        return locationDTOs;
    }

    @Override
    public LocationDTO getLocationById(String locationId) {
        Location location = this.locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found."));
        LocationDTO locationDTO = location.toDTO();
        this.addRelatedData(locationDTO);
        return locationDTO;
    }

    @Override
    public LocationDTO createLocation(UpsertLocationDTO upsertLocationDTO) {
        Location createdLocation = upsertLocationDTO.create();

        if (upsertLocationDTO.getOrganizationId() != null) {
            Organization organization = this.organizationRepository.findById(upsertLocationDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
            organization.getLocations().add(createdLocation);
            this.organizationRepository.save(organization);
            createdLocation.setOrganization(organization);
        }

        createdLocation.setLanguages(new ArrayList<>());
        for (String id : upsertLocationDTO.getLanguages()) {
            Language language = this.languageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Language not found."));
            language.setLocation(createdLocation);
            this.languageRepository.save(language);
            createdLocation.getLanguages().add(language);
        }

        createdLocation.setAddresses(new ArrayList<>());
        for (String id : upsertLocationDTO.getAddresses()) {
            Address address = this.addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found."));
            address.setLocation(createdLocation);
            this.addressRepository.save(address);
            createdLocation.getAddresses().add(address);
        }

        createdLocation.setPhones(new ArrayList<>());
        for (String id  : upsertLocationDTO.getPhones()) {
            Phone phone = this.phoneRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Phone not found."));
            phone.setLocation(createdLocation);
            this.phoneRepository.save(phone);
            createdLocation.getPhones().add(phone);
        }

        createdLocation.setContacts(new ArrayList<>());
        for (String id  : upsertLocationDTO.getContacts()) {
            Contact contact = this.contactRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contact not found."));
            contact.setLocation(createdLocation);
            this.contactRepository.save(contact);
            createdLocation.getContacts().add(contact);
        }

        createdLocation.setAccessibility(new ArrayList<>());
        for (String id  : upsertLocationDTO.getAccessibility()) {
            Accessibility accessibility = this.accessibilityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Accessibility not found."));
            accessibility.setLocation(createdLocation);
            this.accessibilityRepository.save(accessibility);
            createdLocation.getAccessibility().add(accessibility);
        }

        createdLocation.setSchedules(new ArrayList<>());
        for (String id  : upsertLocationDTO.getSchedules()) {
            Schedule schedule = this.scheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule not found."));
            schedule.setLocation(createdLocation);
            this.scheduleRepository.save(schedule);
            createdLocation.getSchedules().add(schedule);
        }

        this.locationRepository.save(createdLocation);
        return this.getLocationById(createdLocation.getId());
    }

    @Override
    public LocationDTO updateLocation(String locationId, UpsertLocationDTO upsertLocationDTO) {
        Location location = this.locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found."));
        Location updatedLocation = upsertLocationDTO.merge(location);
        updatedLocation.setId(locationId);

        if (upsertLocationDTO.getOrganizationId() != null) {
            Organization organization = this.organizationRepository.findById(upsertLocationDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
            organization.getLocations().add(updatedLocation);
            this.organizationRepository.save(organization);
            if (location.getOrganization() != null) {
                location.getOrganization().getLocations().remove(location);
            }
            updatedLocation.setOrganization(organization);
        }

        if (upsertLocationDTO.getLanguages() != null && !upsertLocationDTO.getLanguages().isEmpty()) {
            for (Language language : location.getLanguages()) {
                language.setLocation(null);
                this.languageRepository.save(language);
            }
            updatedLocation.setLanguages(new ArrayList<>());
            for (String id : upsertLocationDTO.getLanguages()) {
                Language language = this.languageRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Language not found."));
                language.setLocation(updatedLocation);
                this.languageRepository.save(language);
                updatedLocation.getLanguages().add(language);
            }
        }

        if (upsertLocationDTO.getAddresses() != null && !upsertLocationDTO.getAddresses().isEmpty()) {
            for (Address address : location.getAddresses()) {
                address.setLocation(null);
                this.addressRepository.save(address);
            }
            updatedLocation.setAddresses(new ArrayList<>());
            for (String id : upsertLocationDTO.getAddresses()) {
                Address address = this.addressRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Address not found."));
                address.setLocation(updatedLocation);
                this.addressRepository.save(address);
                updatedLocation.getAddresses().add(address);
            }
        }

        if (upsertLocationDTO.getPhones() != null && !upsertLocationDTO.getPhones().isEmpty()) {
            for (Phone phone : location.getPhones()) {
                phone.setLocation(null);
                this.phoneRepository.save(phone);
            }
            updatedLocation.setPhones(new ArrayList<>());
            for (String id  : upsertLocationDTO.getPhones()) {
                Phone phone = this.phoneRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Phone not found."));
                phone.setLocation(updatedLocation);
                this.phoneRepository.save(phone);
                updatedLocation.getPhones().add(phone);
            }
        }

        if (upsertLocationDTO.getContacts() != null && !upsertLocationDTO.getContacts().isEmpty()) {
            for (Contact contact : location.getContacts()) {
                contact.setLocation(null);
                this.contactRepository.save(contact);
            }
            updatedLocation.setContacts(new ArrayList<>());
            for (String id  : upsertLocationDTO.getContacts()) {
                Contact contact = this.contactRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Contact not found."));
                contact.setLocation(updatedLocation);
                this.contactRepository.save(contact);
                updatedLocation.getContacts().add(contact);
            }
        }

        if (upsertLocationDTO.getAccessibility() != null && !upsertLocationDTO.getAccessibility().isEmpty()) {
            for (Accessibility accessibility : location.getAccessibility()) {
                accessibility.setLocation(null);
                this.accessibilityRepository.save(accessibility);
            }
            updatedLocation.setAccessibility(new ArrayList<>());
            for (String id  : upsertLocationDTO.getAccessibility()) {
                Accessibility accessibility = this.accessibilityRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Accessibility not found."));
                accessibility.setLocation(updatedLocation);
                this.accessibilityRepository.save(accessibility);
                updatedLocation.getAccessibility().add(accessibility);
            }
        }

        if (upsertLocationDTO.getSchedules() != null && !upsertLocationDTO.getSchedules().isEmpty()) {
            for (Schedule schedule : location.getSchedules()) {
                schedule.setLocation(null);
                this.scheduleRepository.save(schedule);
            }
            updatedLocation.setSchedules(new ArrayList<>());
            for (String id  : upsertLocationDTO.getSchedules()) {
                Schedule schedule = this.scheduleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Schedule not found."));
                schedule.setLocation(updatedLocation);
                this.scheduleRepository.save(schedule);
                updatedLocation.getSchedules().add(schedule);
            }
        }

        this.locationRepository.save(updatedLocation);
        return this.getLocationById(updatedLocation.getId());
    }

    @Override
    public void deleteLocation(String locationId) {
        Location location = this.locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found."));
        this.attributeService.deleteRelatedAttributes(location.getId());
        this.metadataService.deleteRelatedMetadata(location.getId());
        this.locationRepository.delete(location);
    }

    private void addRelatedData(LocationDTO locationDTO) {
        locationDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(locationDTO.getId()));
        locationDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(locationDTO.getId()));
    }
}
