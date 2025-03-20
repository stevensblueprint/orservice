package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;

public interface OrganizationService {
  PaginationDTO<OrganizationDTO.Response> getAllOrganizations(
      String search,
      Boolean full_service,
      Boolean full,
      String taxonomyTerm,
      String taxonomyId,
      Integer page,
      Integer perPage,
      String format
  );

  OrganizationDTO.Response getOrganizationById(String id);
  OrganizationDTO.Response createOrganization(OrganizationDTO.Request requestDto, String updatedBy);
  void deleteOrganization(String id);
}
