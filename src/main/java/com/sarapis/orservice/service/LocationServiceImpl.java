package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.LOCATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.LocationDTO.Request;
import com.sarapis.orservice.dto.LocationDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.mapper.LocationMapper;
import com.sarapis.orservice.model.Location;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;
  private final LanguageService languageService;
  private final AddressService addressService;
  private final ContactService contactService;
  private final AccessibilityService accessibilityService;
  private final PhoneService phoneService;
  private final ScheduleService scheduleService;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createLocation(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Location location = locationMapper.toEntity(dto);
    Location savedLocation = locationRepository.save(location);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedLocation.getId(),
        LOCATION_RESOURCE_TYPE,
        CREATE.name(),
        "location",
        EMPTY_PREVIOUS_VALUE,
        dto.getName(),
        "SYSTEM"
    );

    List<LanguageDTO.Response> savedLanguages = new ArrayList<>();
    if (dto.getLanguages() != null) {
      for (LanguageDTO.Request languageDTO : dto.getLanguages()) {
        languageDTO.setLocationId(savedLocation.getId());
        LanguageDTO.Response savedLanguage = languageService.createLanguage(languageDTO);
        savedLanguages.add(savedLanguage);
      }
    }

    List<AddressDTO.Response> savedAddresses = new ArrayList<>();
    if (dto.getAddresses() != null) {
      for (AddressDTO.Request addressDTO : dto.getAddresses()) {
        addressDTO.setLocationId(savedLocation.getId());
        AddressDTO.Response savedAddress = addressService.createAddress(addressDTO);
        savedAddresses.add(savedAddress);
      }
    }

    List<ContactDTO.Response> savedContacts = new ArrayList<>();
    if (dto.getContacts() != null) {
      for (ContactDTO.Request contactDTO : dto.getContacts()) {
        contactDTO.setLocationId(savedLocation.getId());
        ContactDTO.Response savedContact = contactService.createContact(contactDTO);
        savedContacts.add(savedContact);
      }
    }

    List<AccessibilityDTO.Response> savedAccessibilities = new ArrayList<>();
    if (dto.getAccessibility() != null) {
      for (AccessibilityDTO.Request accessibilityDTO : dto.getAccessibility()) {
        accessibilityDTO.setLocationId(savedLocation.getId());
        AccessibilityDTO.Response savedAccessibility = accessibilityService.createAccessibility(accessibilityDTO);
        savedAccessibilities.add(savedAccessibility);
      }
    }

    List<PhoneDTO.Response> savedPhones = new ArrayList<>();
    if (dto.getPhones() != null) {
      for (PhoneDTO.Request phoneDTO : dto.getPhones()) {
        phoneDTO.setLocationId(savedLocation.getId());
        PhoneDTO.Response savedPhone = phoneService.createPhone(phoneDTO);
        savedPhones.add(savedPhone);
      }
    }

    List<ScheduleDTO.Response> savedSchedules = new ArrayList<>();
    if (dto.getSchedules()!= null) {
      for (ScheduleDTO.Request scheduleDTO : dto.getSchedules()) {
        scheduleDTO.setLocationId(savedLocation.getId());
        ScheduleDTO.Response savedSchedule = scheduleService.createSchedule(scheduleDTO);
        savedSchedules.add(savedSchedule);
      }
    }

    List<MetadataDTO.Response> metadata =
        metadataService.getMetadataByResourceIdAndResourceType(savedLocation.getId(), LOCATION_RESOURCE_TYPE);
    Response response = locationMapper.toResponseDTO(savedLocation);
    response.setLanguages(savedLanguages);
    response.setAddresses(savedAddresses);
    response.setContacts(savedContacts);
    response.setAccessibility(savedAccessibilities);
    response.setPhones(savedPhones);
    response.setSchedules(savedSchedules);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getLocationByOrganizationId(String organizationId) {
    List<Location> locations = locationRepository.findByOrganizationId(organizationId);
    List<LocationDTO.Response> locationDtos = locations.stream().map(locationMapper::toResponseDTO).toList();
    locationDtos = locationDtos.stream().peek(location -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          location.getId(), LOCATION_RESOURCE_TYPE
      );
      location.setLanguages(languageService.getLanguagesByLocationId(location.getId()));
      location.setAddresses(addressService.getAddressesByLocationId(location.getId()));
      location.setContacts(contactService.getContactsByLocationId(location.getId()));
      location.setAccessibility(accessibilityService.getAccessibilityByLocationId(location.getId()));
      location.setPhones(phoneService.getPhonesByLocationId(location.getId()));
      location.setSchedules(scheduleService.getSchedulesByLocationId(location.getId()));
      location.setMetadata(metadata);
    }).toList();
    return locationDtos;
  }
}
