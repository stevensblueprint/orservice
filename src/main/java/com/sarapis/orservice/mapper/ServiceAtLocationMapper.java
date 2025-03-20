package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AT_LOCATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.model.ServiceAtLocation;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ServiceAtLocationMapper {

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private ContactMapper contactMapper;

  @Autowired
  private PhoneMapper phoneMapper;

  @Autowired
  private ScheduleMapper scheduleMapper;


  public abstract ServiceAtLocation toEntity(ServiceAtLocationDTO.Request dto);
  public abstract ServiceAtLocationDTO.Response toResponseDTO(ServiceAtLocation entity);

  @AfterMapping
  protected void setRelations(@MappingTarget ServiceAtLocation serviceAtLocation) {
    if (serviceAtLocation.getContacts() != null) {
      serviceAtLocation.getContacts().forEach(contact ->
          contact.setServiceAtLocation(serviceAtLocation));
    }

    if (serviceAtLocation.getPhones() != null) {
      serviceAtLocation.getPhones().forEach(phone ->
          phone.setServiceAtLocation(serviceAtLocation));
    }

    if (serviceAtLocation.getSchedules() != null) {
      serviceAtLocation.getSchedules().forEach(schedule ->
          schedule.setServiceAtLocation(serviceAtLocation));
    }
  }

  @AfterMapping
  public ServiceAtLocation toEntity(@MappingTarget ServiceAtLocation serviceAtLocation) {
    if (serviceAtLocation.getService().getId() != null) {
      serviceAtLocation.setService(
          serviceRepository.findById(serviceAtLocation.getService().getId())
              .orElseThrow(() -> new IllegalArgumentException("Service not found for service_at_location with ID: " + serviceAtLocation.getId())));
    }
    return serviceAtLocation;
  }

  public ServiceAtLocationDTO.Response toResponseDTO(ServiceAtLocation entity, MetadataService metadataService) {
    ServiceAtLocationDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);

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

    if (entity.getSchedules()!= null) {
      List<ScheduleDTO.Response> enrichedSchedules =
          entity.getSchedules().stream()
              .map(schedule -> scheduleMapper.toResponseDTO(schedule, metadataService)).toList();
      response.setSchedules(enrichedSchedules);
    }

    return response;
  }

  private void enrichMetadata(ServiceAtLocation entity, ServiceAtLocationDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(entity.getId(), SERVICE_AT_LOCATION_RESOURCE_TYPE)
    );
  }
}
