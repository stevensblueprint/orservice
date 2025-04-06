package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Request;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.mapper.ServiceMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.ServiceSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.Parser.parseIntegerAndSet;
import static com.sarapis.orservice.utils.Parser.parseObjectAndSet;
import static com.sarapis.orservice.utils.Parser.parseDateAndSet;
import io.micrometer.common.util.StringUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
  private final MetadataService metadataService;

  private static final int RECORDS_PER_STREAM = 100;

  private static final Map<String, BiConsumer<Service, String>> SERVICE_FIELD_MAP = Map.ofEntries(
          Map.entry("organization", parseObjectAndSet(Service::setOrganization)),
          Map.entry("name", Service::setName),
          Map.entry("alternateName", Service::setAlternateName),
          Map.entry("description", Service::setDescription),
          Map.entry("url", Service::setUrl),
          Map.entry("email", Service::setEmail),
          Map.entry("status", Service::setStatus),
          Map.entry("interpretationServices", Service::setInterpretationServices),
          Map.entry("feesDescription", Service::setFeesDescription),
          Map.entry("waitTime", Service::setWaitTime),
          Map.entry("fees", Service::setFees),
          Map.entry("accreditations", Service::setAccreditations),
          Map.entry("eligibility_description", Service::setEligibility_description),   /* ? */
          Map.entry("minimumAge", parseIntegerAndSet(Service::setMinimumAge)),
          Map.entry("maximumAge", parseIntegerAndSet(Service::setMaximumAge)),
          Map.entry("assuredDate", Service::setAssuredDate),
          Map.entry("assurerEmail", Service::setAssurerEmail),
          Map.entry("licenses", Service::setLicenses),
          Map.entry("alert", Service::setAlert),
          Map.entry("lastModified", parseDateAndSet(Service::setLastModified)),
          Map.entry("phones", parseObjectAndSet(Service::setPhones)),
          Map.entry("schedules", parseObjectAndSet(Service::setSchedules)),
          Map.entry("serviceAreas", parseObjectAndSet(Service::setServiceAreas)),
          Map.entry("serviceAtLocations", parseObjectAndSet(Service::setServiceAtLocations)),
          Map.entry("languages", parseObjectAndSet(Service::setLanguages)),
          Map.entry("funding", parseObjectAndSet(Service::setFunding)),
          Map.entry("costOptions", parseObjectAndSet(Service::setCostOptions)),
          Map.entry("requiredDocuments", parseObjectAndSet(Service::setRequiredDocuments)),
          Map.entry("contacts", parseObjectAndSet(Service::setContacts)),
          Map.entry("additionalUrls", parseObjectAndSet(Service::setAdditionalUrls))
  );

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
  public void undoServiceMetadata(String metadataId) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new RuntimeException("Metadata not found"));

    MetadataUtils.undoMetadata(
            metadata,
            this.metadataRepository,
            this.serviceRepository,
            SERVICE_FIELD_MAP
    );
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
}
