package com.sarapis.orservice.mapper;


import static com.sarapis.orservice.utils.MetadataUtils.LOCATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.model.Location;
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
  private LanguageMapper languageMapper;

  @Autowired
  private AddressMapper addressMapper;

  @Autowired
  private ContactMapper contactMapper;

  @Autowired
  private PhoneMapper phoneMapper;

  @Autowired
  private ScheduleMapper scheduleMapper;


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
      entity.getAccessibility().forEach(accessibility ->
          accessibility.setLocation(entity)
      );
    }

    if (entity.getContacts() != null) {
      entity.getContacts().forEach(contact ->
          contact.setLocation(entity)
      );
    }

    if (entity.getLanguages() != null) {
      entity.getLanguages().forEach(language ->
          language.setLocation(entity)
      );
    }

    if (entity.getAddresses() != null) {
      entity.getAddresses().forEach(address ->
          address.setLocation(entity)
      );
    }

    if (entity.getPhones() != null) {
      entity.getPhones().forEach(phone ->
          phone.setLocation(entity)
      );
    }

    if (entity.getSchedules() != null) {
      entity.getSchedules().forEach(schedule ->
          schedule.setLocation(entity)
      );
    }
  }

  public LocationDTO.Response toResponseDTO(Location entity, MetadataService metadataService) {
    LocationDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
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

  protected void enrichMetadata(Location location, LocationDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            location.getId(), LOCATION_RESOURCE_TYPE
        )
    );
  }
}
