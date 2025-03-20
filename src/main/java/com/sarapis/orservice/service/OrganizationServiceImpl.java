package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
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

  @Override
  @Transactional(readOnly = true)
  public void streamAllOrganizations(String search, Boolean fullService, Boolean full,
      String taxonomyTerm, String taxonomyId, Integer page, Integer perPage,
      Consumer<Response> consumer) {
    Specification<Organization> spec = buildSpecification(search, taxonomyTerm, taxonomyId);
    int currentPage = page;
    while (true) {
      PageRequest pageRequest = PageRequest.of(currentPage, perPage);
      Page<Organization> organizationPage = organizationRepository.findAll(spec, pageRequest);
      List<Organization> organizations = organizationPage.getContent();
      if (organizations.isEmpty()) {
        break;
      }
      organizations.forEach(organization ->
          consumer.accept(organizationMapper.toResponseDTO(organization, metadataService, fullService)));

      if (currentPage >= organizationPage.getTotalPages() - 1) {
        break;
      }
      currentPage++;
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

  private Specification<Organization> buildSpecification(String search, String taxonomyTerm, String taxonomyId) {
    Specification<Organization> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(OrganizationSpecifications.hasSearchTerm(search));
    }
    return spec;
  }

}
