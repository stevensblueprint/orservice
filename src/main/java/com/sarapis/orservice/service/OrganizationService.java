package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import java.util.List;

public interface OrganizationService {
    List<OrganizationDTO> getAllOrganizations();
    OrganizationDTO getOrganizationById(String id);
    OrganizationDTO createOrganization(OrganizationDTO organizationDTO);
    OrganizationDTO updateOrganization(String id, OrganizationDTO organizationDTO);
    void deleteOrganization(String id);
}
