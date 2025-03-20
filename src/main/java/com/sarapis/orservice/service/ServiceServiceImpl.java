package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Request;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.mapper.ServiceMapper;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.ServiceSpecifications;
import io.micrometer.common.util.StringUtils;
import java.time.LocalDate;
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
  private final MetadataRepository metadataRepository;
  private final OrganizationRepository organizationRepository;
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
    Page<ServiceDTO.Response> dtoPage = servicePage.map(service -> serviceMapper.toResponseDTO(service, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceById(String id) {
    Service service = serviceRepository.findById(id).orElseThrow();
    return serviceMapper.toResponseDTO(service, metadataService);
  }

  @Override
  @Transactional
  public Response createService(Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || StringUtils.isBlank(requestDto.getId())) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Service service = serviceMapper.toEntity(requestDto, organizationRepository);
    service.setMetadata(metadataRepository, updatedBy);
    Service savedService = serviceRepository.save(service);
    return serviceMapper.toResponseDTO(savedService, metadataService);
  }
}
