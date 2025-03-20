package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.MetadataType.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AREA_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_RESOURCE_TYPE;


import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Request;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.mapper.ServiceMapper;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.ServiceSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
  private final ServiceRepository serviceRepository;
  private final ServiceMapper serviceMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServices(String search, Integer page, Integer perPage,
      String format, String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean minimal, Boolean full) {
    Specification<Service> spec = Specification.where(null);

    if (search != null && !search.isEmpty()) {
      spec = spec.and(ServiceSpecifications.hasSearchTerm(search));
    }

    if (modifiedAfter != null && !modifiedAfter.isEmpty()) {
      LocalDate modifiedDate = LocalDate.parse(modifiedAfter);
      spec = spec.and(ServiceSpecifications.modifiedAfter(modifiedDate));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
    Page<ServiceDTO.Response> dtoPage = servicePage.map(serviceMapper::toResponseDTO);
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceById(String id) {
    Service service = serviceRepository.findById(id).orElseThrow();
    return serviceMapper.toResponseDTO(service);
  }

  @Override
  @Transactional
  public Response createService(Request requestDto) {
    if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Service service = serviceMapper.toEntity(requestDto);
    Service savedService = serviceRepository.save(service);

    MetadataUtils.createMetadataEntry(
        metadataService,
        savedService.getId(),
        SERVICE_AREA_RESOURCE_TYPE,
        CREATE.name(),
        "service",
        EMPTY_PREVIOUS_VALUE,
        service.getName(),
        "SYSTEM"
    );

    List<MetadataDTO.Response> metadata =
        metadataService.getMetadataByResourceIdAndResourceType(requestDto.getId(), SERVICE_RESOURCE_TYPE);
    Response response = serviceMapper.toResponseDTO(savedService);
    response.setMetadata(metadata);
    return serviceMapper.toResponseDTO(savedService);
  }
}
