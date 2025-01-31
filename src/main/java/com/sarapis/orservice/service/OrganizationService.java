package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface OrganizationService {
    List<OrganizationDTO> getAllOrganizations();

    OrganizationDTO getOrganizationById(String organizationId);

    OrganizationDTO createOrganization(UpsertOrganizationDTO upsertOrganizationDTO);

    OrganizationDTO updateOrganization(String organizationId, UpsertOrganizationDTO upsertOrganizationDTO);

    void deleteOrganization(String organizationId);

    ByteArrayInputStream loadCSV();

}
