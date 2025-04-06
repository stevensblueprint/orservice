package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.FieldMap.ORGANIZATION_FIELD_MAP;

import java.util.List;
import java.util.UUID;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final MetadataService metadataService;
  private final MetadataRepository metadataRepository;

  private static final boolean RETURN_FULL_SERVICE = true;
  private static final int RECORDS_PER_STREAM = 100;


  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllOrganizations(String search, Boolean full_service,
      Boolean full, String taxonomyTerm, String taxonomyId, Integer page, Integer perPage) {
    Specification<Organization> spec = buildSpecification(search, taxonomyTerm, taxonomyId);

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);
    Page<OrganizationDTO.Response> dtoPage = organizationPage
        .map(organization -> organizationMapper.toResponseDTO(organization, metadataService, full_service));

    return PaginationDTO.fromPage(dtoPage);
  }

  // In OrganizationServiceImpl.java
  @Override
  @Transactional(readOnly = true)
  public void streamAllOrganizations(String search, Boolean fullService, Boolean full,
      String taxonomyTerm, String taxonomyId, Consumer<OrganizationDTO.Response> consumer) {

    Specification<Organization> spec = buildSpecification(search, taxonomyTerm, taxonomyId);
    int currentPage = 0;
    boolean hasMoreData = true;

    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);

      List<Organization> organizations = organizationPage.getContent();

      if (organizations.isEmpty()) {
        hasMoreData = false;
      } else {
        organizations.forEach(organization ->
            consumer.accept(organizationMapper.toResponseDTO(organization, metadataService, fullService))
        );
        if (currentPage >= organizationPage.getTotalPages() - 1) {
          hasMoreData = false;
        } else {
          currentPage++;
        }
      }
    }
  }


  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id, Boolean fullService) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    return organizationMapper.toResponseDTO(organization, metadataService, fullService);
  }

  @Override
  @Transactional
  public Response createOrganization(Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Organization organization = organizationMapper.toEntity(requestDto);
    organization.setMetadata(metadataRepository, updatedBy);
    Organization savedOrganization = organizationRepository.save(organization);
    return organizationMapper.toResponseDTO(savedOrganization, metadataService, RETURN_FULL_SERVICE);
  }

  @Override
  @Transactional
  public void deleteOrganization(String id) {
    organizationRepository.deleteById(id);
    log.info("Deleted organization with id: {}", id);
  }

  @Override
  @Transactional
  public Response undoOrganizationMetadata(String metadataId, String updatedBy) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new RuntimeException("Metadata not found"));

    Organization reverted = MetadataUtils.undoMetadata(
        metadata,
        this.metadataRepository,
        this.organizationRepository,
        ORGANIZATION_FIELD_MAP,
        updatedBy
    );
    return organizationMapper.toResponseDTO(reverted, metadataService, RETURN_FULL_SERVICE);
  }
  private Specification<Organization> buildSpecification(String search, String taxonomyTerm, String taxonomyId) {
    Specification<Organization> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(OrganizationSpecifications.hasSearchTerm(search));
    }
    return spec;
  }

}
