package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationDTO.Request;
import com.sarapis.orservice.dto.OrganizationDTO.Response;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.mapper.OrganizationMapper;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.OrganizationSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
  private OrganizationRepository organizationRepository;
  private OrganizationMapper organizationMapper;

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
    Page<OrganizationDTO.Response> dtoPage = organizationPage.map(organizationMapper::toResponseDTO);
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getOrganizationById(String id) {
    Organization organization = organizationRepository.findById(id).orElseThrow();
    return organizationMapper.toResponseDTO(organization);
  }

  @Override
  @Transactional
  public Response createOrganization(Request requestDto) {
    return null;
  }
}
