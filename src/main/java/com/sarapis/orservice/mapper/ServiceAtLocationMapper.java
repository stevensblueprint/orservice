package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AT_LOCATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.model.Schedule;
import com.sarapis.orservice.model.ServiceAtLocation;
import com.sarapis.orservice.repository.ContactRepository;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.repository.ScheduleRepository;
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
  private ContactRepository contactRepository;

  @Autowired
  private PhoneRepository phoneRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

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
    if (serviceAtLocation.getService().getId() != null) {
      serviceAtLocation.setService(
          serviceRepository.findById(serviceAtLocation.getService().getId()).orElseThrow(
              () -> new RuntimeException("Service not found: " + serviceAtLocation.getService().getId())
          )
      );
    }

    if (serviceAtLocation.getContacts() != null) {
      List<Contact> managedContacts = serviceAtLocation.getContacts().stream()
          .map(contact -> {
            if (contact.getId() != null) {
              return contactRepository.findById(contact.getId())
                  .orElse(contact);
            }
            return contact;
          })
          .peek(contact -> contact.setServiceAtLocation(serviceAtLocation)).toList();

      serviceAtLocation.setContacts(managedContacts);
    }

    if (serviceAtLocation.getPhones() != null) {
      List<Phone> managedPhones = serviceAtLocation.getPhones().stream()
          .map(phone -> {
            if (phone.getId() != null) {
              return phoneRepository.findById(phone.getId())
                  .orElse(phone);
            }
            return phone;
          })
          .peek(phone -> phone.setServiceAtLocation(serviceAtLocation)).toList();

      serviceAtLocation.setPhones(managedPhones);
    }

    if (serviceAtLocation.getSchedules() != null) {
      List<Schedule> managedSchedules = serviceAtLocation.getSchedules().stream()
          .map(schedule -> {
            if (schedule.getId() != null) {
              return scheduleRepository.findById(schedule.getId())
                  .orElse(schedule);
            }
            return schedule;
          })
          .peek(schedule -> schedule.setServiceAtLocation(serviceAtLocation)).toList();

      serviceAtLocation.setSchedules(managedSchedules);
    }
  }

  public ServiceAtLocationDTO.Response toResponseDTO(ServiceAtLocation entity, MetadataService metadataService) {
    ServiceAtLocationDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        ServiceAtLocation::getId,
        ServiceAtLocationDTO.Response::setMetadata,
        SERVICE_AT_LOCATION_RESOURCE_TYPE,
        metadataService
    );

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
}
