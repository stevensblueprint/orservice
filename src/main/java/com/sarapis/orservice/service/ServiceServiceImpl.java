package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Request;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.ServiceMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.ServiceSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.FieldMap.SERVICE_FIELD_MAP;
import io.micrometer.common.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.zip.ZipOutputStream;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
  private final ServiceRepository serviceRepository;
  private final ServiceMapper serviceMapper;
  private final MetadataRepository metadataRepository;
  private final MetadataService metadataService;

  private static final int RECORDS_PER_STREAM = 100;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServices(String search, Integer page, Integer perPage,
      String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean minimal, Boolean full) {
    Specification<Service> spec = buildSpecification(search, modifiedAfter, taxonomyTermId, taxonomyId, organizationId);
    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
    Page<ServiceDTO.Response> dtoPage = servicePage.map(service -> serviceMapper.toResponseDTO(service, metadataService));
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public void streamAllServices(String search, String taxonomyTermId, String taxonomyId,
      String organizationId, String modifiedAfter, Boolean minimal, Boolean full,
      Consumer<Response> consumer) {
    Specification<Service> spec = buildSpecification(search, modifiedAfter, taxonomyTermId, taxonomyId, organizationId);
    int currentPage = 0;
    boolean hasMoreData = true;
    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
      List<Service> services = servicePage.getContent();

      if (services.isEmpty()) {
        hasMoreData = false;
      } else {
        services.forEach(service ->
            consumer.accept(serviceMapper.toResponseDTO(service, metadataService))
        );
        if (currentPage >= servicePage.getTotalPages() - 1) {
          hasMoreData = false;
        } else {
          currentPage++;
        }
      }
    }
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
    Service service = serviceMapper.toEntity(requestDto);
    service.setMetadata(metadataRepository, updatedBy);
    Service savedService = serviceRepository.save(service);
    return serviceMapper.toResponseDTO(savedService, metadataService);
  }

  @Override
  @Transactional
  public Response updateService(String id, Request updatedDto, String updatedBy) {
    if(!this.serviceRepository.existsById(id)) {
      throw new ResourceNotFoundException("Service", id);
    }

    updatedDto.setId(id);
    Service newService = this.serviceMapper.toEntity(updatedDto);

    Service updatedService = this.serviceRepository.save(newService);
    return this.serviceMapper.toResponseDTO(updatedService, this.metadataService);
  }

  @Override
  @Transactional
  public Response undoServiceMetadata(String metadataId, String updatedBy) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new ResourceNotFoundException("Metadata", metadataId));

    Service reverted = MetadataUtils.undoMetadata(
            metadata,
            this.metadataRepository,
            this.serviceRepository,
            SERVICE_FIELD_MAP,
            updatedBy
    );
    return serviceMapper.toResponseDTO(reverted, metadataService);
  }

  @Override
  @Transactional
  public Response undoServiceMetadataBatch(List<Metadata> metadataList, String updatedBy) {
    Service reverted = MetadataUtils.undoMetadataBatch(
            metadataList,
            this.metadataRepository,
            this.serviceRepository,
            SERVICE_FIELD_MAP,
            updatedBy
    );
    return serviceMapper.toResponseDTO(reverted, metadataService);
  }

  private Specification<Service> buildSpecification(String search, String modifiedAfter,
      String taxonomyTermId, String taxonomyId, String organizationId) {
    Specification<Service> spec = Specification.where(null);

    if (search!= null &&!search.isEmpty()) {
      spec = spec.and(ServiceSpecifications.hasSearchTerm(search));
    }

    if (modifiedAfter!= null &&!modifiedAfter.isEmpty()) {
      LocalDate modifiedDate = LocalDate.parse(modifiedAfter);
      spec = spec.and(ServiceSpecifications.modifiedAfter(modifiedDate));
    }

    return spec;
  }

  @Override
  public long writeCsv(ZipOutputStream zipOutputStream) throws IOException {
    return 0;
  }

  @Override
  public long writePdf(ZipOutputStream zipOutputStream) throws IOException {
    return 0;
  }

  @Override
  public void readCsv(MultipartFile file, String updatedBy, List<String> metadataIds) throws IOException {

  }
}
