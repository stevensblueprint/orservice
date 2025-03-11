package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.DEFAULT_CREATED_BY;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
import java.util.List;
import java.util.UUID;
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

    Page<OrganizationDTO.Response> dtoPage = organizationPage.map(organization -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          organization.getId(), ORGANIZATION_RESOURCE_TYPE
      );
      Response response = organizationMapper.toResponseDTO(organization);
      response.setMetadata(metadata);
      return response;
    });

    return PaginationDTO.fromPage(dtoPage);
  }


  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id) {
    Organization organization = organizationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
    List<MetadataDTO.Response> metadata =
        metadataService.getMetadataByResourceIdAndResourceType(id, ORGANIZATION_RESOURCE_TYPE);
    Response response = organizationMapper.toResponseDTO(organization);
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional
  public OrganizationDTO.Response createOrganization(OrganizationDTO.Request requestDto) {
    if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Organization organization = organizationMapper.toEntity(requestDto);
    Organization savedOrganization = organizationRepository.save(organization);

    metadataService.createMetadata(
        null,
        savedOrganization,
        ORGANIZATION_RESOURCE_TYPE,
        CREATE,
        DEFAULT_CREATED_BY
    );

    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedOrganization.getId(),
        ORGANIZATION_RESOURCE_TYPE
    );
    OrganizationDTO.Response response = organizationMapper.toResponseDTO(savedOrganization);
    response.setMetadata(metadata);
    return response;
  }

  @Override
  public Response updateOrganization(String id, Request requestDto) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    OrganizationDTO.Response previousState = organizationMapper.toResponseDTO(organization);
    return null;
  }
}
