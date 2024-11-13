package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationRepository organizationRepository;

  private OrganizationDTO mapToDTO(Organization organization) {
    return new OrganizationDTO(
        organization.getId(),
        organization.getName(),
        organization.getAlternateName(),
        organization.getDescription(),
        organization.getEmail(),
        organization.getWebsite(),
        organization.getAdditionalWebsites(),
        organization.getYearIncorporated(),
        organization.getLegalStatus(),
        organization.getLogo(),
        organization.getUri(),
        organization.getParentOrganization()
    );
  }

  private Organization mapToEntity(OrganizationDTO organizationDTO) {
    return new Organization(
        organizationDTO.getId(),
        organizationDTO.getName(),
        organizationDTO.getAlternateName(),
        organizationDTO.getDescription(),
        organizationDTO.getEmail(),
        organizationDTO.getWebsite(),
        organizationDTO.getAdditionalWebsites(),
        organizationDTO.getYearIncorporated(),
        organizationDTO.getLegalStatus(),
        organizationDTO.getLogo(),
        organizationDTO.getUri(),
        organizationDTO.getParentOrganization()
    );
  }

  @Autowired
  public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  @Override
  public List<OrganizationDTO> getAllOrganizations() {
    return organizationRepository.findAll().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public OrganizationDTO getOrganizationById(Long id) {
    Organization organization =  organizationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Organization not found"));
    return mapToDTO(organization);
  }

  @Override
  public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
    Organization organization = mapToEntity(organizationDTO);
    Organization savedOrganization = organizationRepository.save(organization);
    return mapToDTO(savedOrganization);
  }

  @Override
  public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
    Organization existingOrganization = organizationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Could not find organization"));
    existingOrganization.setName(organizationDTO.getName());
    existingOrganization.setAlternateName(organizationDTO.getAlternateName());
    existingOrganization.setDescription(organizationDTO.getDescription());
    existingOrganization.setEmail(organizationDTO.getEmail());
    existingOrganization.setWebsite(organizationDTO.getWebsite());
    Organization updatedOrganization = organizationRepository.save(existingOrganization);
    return mapToDTO(updatedOrganization);
  }

  @Override
  public void deleteOrganization(Long id) {
    Organization organization = organizationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Could not find organization"));
    organizationRepository.delete(organization);
  }
}
