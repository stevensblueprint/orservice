package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationRepository organizationRepository;

  @Autowired
  public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  @Override
  public List<OrganizationDTO> getAllOrganizations() {
    return organizationRepository.findAll().stream()
        .map(Organization::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public OrganizationDTO getOrganizationById(String id) {
    Organization organization =  organizationRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> new RuntimeException("Organization not found"));
    return organization.toDTO();
  }

  @Override
  public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
    Organization organization = organizationDTO.toEntity();
    Organization savedOrganization = organizationRepository.save(organization);
    return savedOrganization.toDTO();
  }

  @Override
  public OrganizationDTO updateOrganization(String id, OrganizationDTO organizationDTO) {
    Organization existingOrganization = organizationRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> new RuntimeException("Could not find organization"));
    existingOrganization.setName(organizationDTO.getName());
    existingOrganization.setAlternateName(organizationDTO.getAlternateName());
    existingOrganization.setDescription(organizationDTO.getDescription());
    existingOrganization.setEmail(organizationDTO.getEmail());
    existingOrganization.setWebsite(organizationDTO.getWebsite());
    Organization updatedOrganization = organizationRepository.save(existingOrganization);
    return updatedOrganization.toDTO();
  }

  @Override
  public void deleteOrganization(String id) {
    Organization organization = organizationRepository.findById(Long.parseLong(id))
        .orElseThrow(() -> new RuntimeException("Could not find organization"));
    organizationRepository.delete(organization);
  }
}
