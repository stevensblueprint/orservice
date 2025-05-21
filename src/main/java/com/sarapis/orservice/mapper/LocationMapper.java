package com.sarapis.orservice.mapper;


import static com.sarapis.orservice.utils.MetadataUtils.LOCATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.model.Accessibility;
import com.sarapis.orservice.model.Address;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.model.Location;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.model.Schedule;
import com.sarapis.orservice.repository.AccessibilityRepository;
import com.sarapis.orservice.repository.AddressRepository;
import com.sarapis.orservice.repository.ContactRepository;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.repository.ScheduleRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class LocationMapper {

  @Autowired
  private AccessibilityMapper accessibilityMapper;
  @Autowired
  private AccessibilityRepository accessibilityRepository;

  @Autowired
  private LanguageMapper languageMapper;
  @Autowired
  private LanguageRepository languageRepository;

  @Autowired
  private AddressMapper addressMapper;
  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private ContactMapper contactMapper;
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private PhoneMapper phoneMapper;
  @Autowired
  private PhoneRepository phoneRepository;

  @Autowired
  private ScheduleMapper scheduleMapper;
  @Autowired
  private ScheduleRepository scheduleRepository;


  @Mapping(target = "organization.id", source = "organizationId")
  public abstract Location toEntity(LocationDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  public abstract  LocationDTO.Response toResponseDTO(Location entity);

  @AfterMapping
  protected void toEntity(LocationDTO.Request dto, @MappingTarget() Location entity) {
    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }

    if (entity.getAccessibility() != null) {
      List<Accessibility> managedAccessibility = entity.getAccessibility().stream()
          .map(accessibility -> {
            if (accessibility.getId() != null) {
              return accessibilityRepository.findById(accessibility.getId())
                  .orElse(accessibility);
            }
            return accessibility;
          })
          .peek(accessibility -> accessibility.setLocation(entity))
          .toList();
      entity.setAccessibility(managedAccessibility);
    }

    if (entity.getContacts() != null) {
      List<Contact> managedContacts = entity.getContacts().stream()
          .map(contact -> {
            if (contact.getId()!= null) {
              return contactRepository.findById(contact.getId())
                 .orElse(contact);
            }
            return contact;
          })
          .peek(contact -> contact.setLocation(entity)).toList();
      entity.setContacts(managedContacts);
    }

    if (entity.getLanguages() != null) {
      List<Language> managedLanguages = entity.getLanguages().stream()
          .map(language -> {
            if (language.getId()!= null) {
              return languageRepository.findById(language.getId())
                 .orElse(language);
            }
            return language;
          })
          .peek(language -> language.setLocation(entity)).toList();
      entity.setLanguages(managedLanguages);
    }

    if (entity.getAddresses() != null) {
      List<Address> managedAddresses = entity.getAddresses().stream()
          .map(address -> {
            if (address.getId()!= null) {
              return addressRepository.findById(address.getId())
                 .orElse(address);
            }
            return address;
          })
          .peek(address -> address.setLocation(entity)).toList();
      entity.setAddresses(managedAddresses);
    }

    if (entity.getPhones() != null) {
      List<Phone> managedPhones = entity.getPhones().stream()
          .map(phone -> {
            if (phone.getId()!= null) {
              return phoneRepository.findById(phone.getId())
                 .orElse(phone);
            }
            return phone;
          })
          .peek(phone -> phone.setLocation(entity)).toList();
      entity.setPhones(managedPhones);
    }

    if (entity.getSchedules() != null) {
      List<Schedule> managedSchedules = entity.getSchedules().stream()
          .map(schedule -> {
            if (schedule.getId()!= null) {
              return scheduleRepository.findById(schedule.getId())
                 .orElse(schedule);
            }
            return schedule;
          })
          .peek(schedule -> schedule.setLocation(entity)).toList();
      entity.setSchedules(managedSchedules);
    }
  }

  public LocationDTO.Response toResponseDTO(Location entity, MetadataService metadataService) {
    LocationDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Location::getId,
        LocationDTO.Response::setMetadata,
        LOCATION_RESOURCE_TYPE,
        metadataService
    );

    if (entity.getAccessibility() != null) {
      List<AccessibilityDTO.Response> enrichedAccessibility =
          entity.getAccessibility().stream()
              .map(accessibility -> accessibilityMapper.toResponseDTO(accessibility, metadataService)).toList();
      response.setAccessibility(enrichedAccessibility);
    }

    if (entity.getLanguages() != null) {
      List<LanguageDTO.Response> enrichedLanguages =
          entity.getLanguages().stream()
              .map(language -> languageMapper.toResponseDTO(language, metadataService)).toList();
      response.setLanguages(enrichedLanguages);
    }

    if (entity.getAddresses() != null) {
      List<AddressDTO.Response> enrichedAddresses =
          entity.getAddresses().stream()
              .map(address -> addressMapper.toResponseDTO(address, metadataService)).toList();
      response.setAddresses(enrichedAddresses);
    }

    if (entity.getContacts() != null) {
      List<ContactDTO.Response> enrichedContacts =
          entity.getContacts().stream()
             .map(contact -> contactMapper.toResponseDTO(contact, metadataService)).toList();
      response.setContacts(enrichedContacts);
    }

    if (entity.getPhones() != null) {
      List<PhoneDTO.Response> enrichedPhones =
          entity.getPhones().stream()
             .map(phone -> phoneMapper.toResponseDTO(phone, metadataService)).toList();
      response.setPhones(enrichedPhones);
    }

    if (entity.getSchedules() != null) {
      List<ScheduleDTO.Response> enrichedSchedules =
          entity.getSchedules().stream()
             .map(schedule -> scheduleMapper.toResponseDTO(schedule, metadataService)).toList();
      response.setSchedules(enrichedSchedules);
    }

    return response;
  }

}
