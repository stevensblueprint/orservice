package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.core.Organization;
import java.util.List;
import java.util.Optional;

public interface OrganizationService {
  List<OrganizationDTO> getAllOrganizations();

  OrganizationDTO getOrganizationById(Long id);

  OrganizationDTO createOrganization(OrganizationDTO organizationDTO);

  OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO);

  void deleteOrganization(Long id);

}
