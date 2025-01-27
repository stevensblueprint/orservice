package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface OrganizationService {
    List<OrganizationDTO> getAllOrganizations(String search);

    OrganizationDTO getOrganizationById(String organizationId);

    OrganizationDTO createOrganization(OrganizationDTO organizationDTO);

    OrganizationDTO updateOrganization(String organizationId, OrganizationDTO organizationDTO);

    void deleteOrganization(String organizationId);

    ByteArrayInputStream loadCSV();
}
