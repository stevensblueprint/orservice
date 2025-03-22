package com.sarapis.orservice.service;


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
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
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

  private static final int RECORDS_PER_STREAM = 100;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServicesAtLocation(String search, String taxonomyTermId,
      String taxonomyId, String organizationId, String modifiedAfter, Boolean full, Integer page,
      Integer perPage, String postcode, String proximity) {
    Specification<ServiceAtLocation> spec = buildSpecification(search, taxonomyTermId, taxonomyId,
        organizationId, modifiedAfter, postcode, proximity);

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<ServiceAtLocation> serviceAtLocationPage = serviceAtLocationRepository.findAll(spec, pageable);
    Page<ServiceAtLocationDTO.Response> dtoPage =
        serviceAtLocationPage.map(serviceAtLocation -> serviceAtLocationMapper.toResponseDTO(serviceAtLocation, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public void streamAllServicesAtLocation(String search, String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean full, String postcode, String proximity, Consumer<Response> consumer) {
    Specification<ServiceAtLocation> spec = buildSpecification(search, taxonomyTermId, taxonomyId,
        organizationId, modifiedAfter, postcode, proximity);
    int currentPage = 0;
    boolean hasMoreData = true;
    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<ServiceAtLocation> services = serviceAtLocationRepository.findAll(spec, pageable);
      List<ServiceAtLocation> serviceList = services.getContent();
      if (serviceList.isEmpty()) {
        hasMoreData = false;
      } else {
        services.forEach(service -> {
          consumer.accept(serviceAtLocationMapper.toResponseDTO(service, metadataService));
        });
          if (currentPage >= services.getTotalPages() - 1) {
            hasMoreData = false;
          } else {
            currentPage++;
          }
      }
    }
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

  private Specification<ServiceAtLocation> buildSpecification(String search, String taxonomyTermId,
      String taxonomyId, String organizationId, String modifiedAfter, String postcode, String proximity) {
    Specification<ServiceAtLocation> spec = Specification.where(null);
    if (search!= null &&!search.isEmpty()) {
      spec = spec.and(ServiceAtLocationSpecifications.hasSearchTerm(search));
    }
    return spec;
  }
}
