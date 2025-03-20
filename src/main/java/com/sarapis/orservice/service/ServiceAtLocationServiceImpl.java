package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.MetadataType.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AT_LOCATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Request;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Response;
import com.sarapis.orservice.mapper.ServiceAtLocationMapper;
import com.sarapis.orservice.model.ServiceAtLocation;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.ServiceAtLocationSpecifications;
import io.micrometer.common.util.StringUtils;
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
  private final MetadataService metadataService;
  private final MetadataRepository metadataRepository;

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
    Page<ServiceAtLocationDTO.Response> dtoPage =
        serviceAtLocationPage.map(serviceAtLocation -> serviceAtLocationMapper.toResponseDTO(serviceAtLocation, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceAtLocationById(String id) {
    ServiceAtLocation service = serviceAtLocationRepository.findById(id).orElseThrow();
    return serviceAtLocationMapper.toResponseDTO(service, metadataService);
  }

  @Override
  @Transactional
  public Response createServiceAtLocation(Request dto, String updatedBy) {
    if (dto.getId() == null || StringUtils.isBlank(dto.getId())) {
      dto.setId(UUID.randomUUID().toString());
    }
    ServiceAtLocation serviceAtLocation = serviceAtLocationMapper.toEntity(dto);
    serviceAtLocation.setMetadata(metadataRepository, updatedBy);
    ServiceAtLocation savedServiceAtLocation = serviceAtLocationRepository.save(serviceAtLocation);
    return serviceAtLocationMapper.toResponseDTO(savedServiceAtLocation, metadataService);
  }
}
