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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.Parser.parseIntegerAndSet;
import static com.sarapis.orservice.utils.Parser.parseObjectAndSet;

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

  private static final Map<String, BiConsumer<Organization, String>> ORGANIZATION_FIELD_MAP = Map.ofEntries(
          Map.entry("name", Organization::setName),
          Map.entry("alternateName", Organization::setAlternateName),
          Map.entry("description", Organization::setDescription),
          Map.entry("email", Organization::setEmail),
          Map.entry("website", Organization::setWebsite),
          Map.entry("taxStatus", Organization::setTaxStatus),
          Map.entry("taxId", Organization::setTaxId),
          Map.entry("yearIncorporated", parseIntegerAndSet(Organization::setYearIncorporated)),
          Map.entry("legalStatus", Organization::setLegalStatus),
          Map.entry("logo", Organization::setLogo),
          Map.entry("uri", Organization::setUri),
          Map.entry("services", parseObjectAndSet(Organization::setServices)),
          Map.entry("additionalWebsites", parseObjectAndSet(Organization::setAdditionalWebsites)),
          Map.entry("funding", parseObjectAndSet(Organization::setFunding)),
          Map.entry("contacts", parseObjectAndSet(Organization::setContacts)),
          Map.entry("phones", parseObjectAndSet(Organization::setPhones)),
          Map.entry("programs", parseObjectAndSet(Organization::setPrograms)),
          Map.entry("organizationIdentifiers", parseObjectAndSet(Organization::setOrganizationIdentifiers)),
          Map.entry("locations", parseObjectAndSet(Organization::setLocations))
  );

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllOrganizations(String search, Boolean full_service,
      Boolean full, String taxonomyTerm, String taxonomyId, Integer page, Integer perPage,
      String format) {
    Specification<Organization> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(OrganizationSpecifications.hasSearchTerm(search));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Organization> organizationPage = organizationRepository.findAll(spec, pageable);

    Page<OrganizationDTO.Response> dtoPage = organizationPage
        .map(organization -> organizationMapper.toResponseDTO(organization, metadataService));

    return PaginationDTO.fromPage(dtoPage);
  }


  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id, Boolean fullService) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    return organizationMapper.toResponseDTO(organization, metadataService);
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
    return organizationMapper.toResponseDTO(savedOrganization, metadataService);
  }

  @Override
  @Transactional
  public void deleteOrganization(String id) {
    organizationRepository.deleteById(id);
    log.info("Deleted organization with id: {}", id);
  }

  @Override
  @Transactional
  public void undoOrganizationMetadata(String metadataId) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new RuntimeException("Metadata not found"));

    /*
    String resType = metadata.getResourceType();
    if(!resType.equals(ORGANIZATION_RESOURCE_TYPE)) {
      throw new RuntimeException("");
    }
    */

    MetadataUtils.undoMetadata(
        metadata,
        this.metadataRepository,
        this.organizationRepository,
        ORGANIZATION_FIELD_MAP
    );
  }
}
