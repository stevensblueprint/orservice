package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AT_LOCATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Request;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Response;
import com.sarapis.orservice.mapper.ServiceAtLocationMapper;
import com.sarapis.orservice.model.ServiceAtLocation;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.ServiceAtLocationSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {

  private final ServiceAtLocationRepository serviceAtLocationRepository;
  private final ServiceAtLocationMapper serviceAtLocationMapper;
  private final ContactService contactService;
  private final PhoneService phoneService;
  private final ScheduleService scheduleService;
  private final MetadataService metadataService;
  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServicesAtLocation(String search, String taxonomyTermId,
      String taxonomyId, String organizationId, String modifiedAfter, Boolean full, Integer page,
      Integer perPage, String format, String postcode, String proximity) {
    Specification<ServiceAtLocation> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(ServiceAtLocationSpecifications.hasSearchTerm(search));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<ServiceAtLocation> serviceAtLocationPage = serviceAtLocationRepository.findAll(spec, pageable);
    Page<ServiceAtLocationDTO.Response> dtoPage = serviceAtLocationPage.map(service -> {
      ServiceAtLocationDTO.Response response = serviceAtLocationMapper.toResponseDTO(service);
      response.setContacts(contactService.getContactsByServiceAtLocationId(service.getId()));
      response.setPhones(phoneService.getPhonesByServiceAtLocationId(service.getId()));
      response.setSchedules(scheduleService.getSchedulesByServiceAtLocationId(service.getId()));
      response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(service.getId(), SERVICE_AT_LOCATION_RESOURCE_TYPE));
      return response;
    });
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional
  public Response createServiceAtLocation(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    ServiceAtLocation serviceAtLocation = serviceAtLocationMapper.toEntity(dto);
    ServiceAtLocation savedServiceAtLocation = serviceAtLocationRepository.save(serviceAtLocation);

    MetadataUtils.createMetadataEntry(
        metadataService,
        savedServiceAtLocation.getId(),
        SERVICE_AT_LOCATION_RESOURCE_TYPE,
        CREATE.name(),
        "service_at_location",
        EMPTY_PREVIOUS_VALUE,
        dto.getDescription(),
        "SYSTEM"
    );
    List<ContactDTO.Response> savedContacts = new ArrayList<>();
    if (dto.getContacts() != null) {
      for (ContactDTO.Request contactDTO : dto.getContacts()) {
        contactDTO.setServiceAtLocationId(dto.getId());
        ContactDTO.Response savedContact = contactService.createContact(contactDTO);
        savedContacts.add(savedContact);
      }
    }

    List<PhoneDTO.Response> savedPhones = new ArrayList<>();
    if (dto.getPhones() != null) {
      for (PhoneDTO.Request phoneDTO : dto.getPhones()) {
        phoneDTO.setServiceAtLocationId(dto.getId());
        PhoneDTO.Response savedPhone = phoneService.createPhone(phoneDTO);
        savedPhones.add(savedPhone);
      }
    }

    List<ScheduleDTO.Response> savedSchedules = new ArrayList<>();
    if (dto.getSchedules() != null) {
      for (ScheduleDTO.Request scheduleDTO : dto.getSchedules()) {
        scheduleDTO.setServiceAtLocationId(dto.getId());
        ScheduleDTO.Response savedSchedule = scheduleService.createSchedule(scheduleDTO);
        savedSchedules.add(savedSchedule);
      }
    }

    ServiceAtLocationDTO.Response response = serviceAtLocationMapper.toResponseDTO(savedServiceAtLocation);
    response.setContacts(savedContacts);
    response.setPhones(savedPhones);
    response.setSchedules(savedSchedules);
    response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(
        savedServiceAtLocation.getId(),
        SERVICE_AT_LOCATION_RESOURCE_TYPE
    ));
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceAtLocationById(String id) {
    ServiceAtLocation service = serviceAtLocationRepository.findById(id).orElseThrow();
    ServiceAtLocationDTO.Response response = serviceAtLocationMapper.toResponseDTO(service);
    response.setContacts(contactService.getContactsByServiceAtLocationId(service.getId()));
    response.setPhones(phoneService.getPhonesByServiceAtLocationId(service.getId()));
    response.setSchedules(scheduleService.getSchedulesByServiceAtLocationId(service.getId()));
    response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(service.getId(), SERVICE_AT_LOCATION_RESOURCE_TYPE));
    return response;
  }
}
