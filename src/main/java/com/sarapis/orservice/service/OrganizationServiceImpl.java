package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   AttributeService attributeService,
                                   MetadataService metadataService) {
        this.organizationRepository = organizationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations() {
        List<OrganizationDTO> organizationDTOs = this.organizationRepository.findAll()
                .stream().map(Organization::toDTO).toList();
        organizationDTOs.forEach(this::addRelatedData);
        return organizationDTOs;
    }

    @Override
    public OrganizationDTO getOrganizationById(String organizationId) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        OrganizationDTO organizationDTO = organization.toDTO();
        this.addRelatedData(organizationDTO);
        return organizationDTO;
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization parentOrganization = null;

        if (organizationDTO.getParentOrganizationId() != null) {
            parentOrganization = this.organizationRepository.findById(organizationDTO.getParentOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Parent organization not found."));
        }

        Organization organization = organizationDTO.toEntity(parentOrganization);
        organizationDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(organization.getId(), attributeDTO));
        organizationDTO.getMetadata()
                .forEach(e -> this.metadataService.createMetadata(organization.getId(), e));

        Organization createdOrganization = this.organizationRepository.save(organization);
        return this.getOrganizationById(createdOrganization.getId());
    }

    @Override
    public OrganizationDTO updateOrganization(String organizationId, OrganizationDTO organizationDTO) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));

        organization.setName(organizationDTO.getName());
        organization.setAlternateName(organizationDTO.getAlternateName());
        organization.setDescription(organizationDTO.getDescription());
        organization.setEmail(organizationDTO.getEmail());
        organization.setWebsite(organizationDTO.getWebsite());
        organization.setTaxStatus(organizationDTO.getTaxStatus());
        organization.setTaxId(organizationDTO.getTaxId());
        organization.setYearIncorporated(organizationDTO.getYearIncorporated());
        organization.setLegalStatus(organizationDTO.getLegalStatus());
        organization.setLogo(organizationDTO.getLogo());
        organization.setUri(organizationDTO.getUri());

        Organization updatedOrganization = this.organizationRepository.save(organization);
        return this.getOrganizationById(updatedOrganization.getId());
    }

    @Override
    public void deleteOrganization(String organizationId) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        this.attributeService.deleteRelatedAttributes(organization.getId());
        this.metadataService.deleteRelatedMetadata(organization.getId());
        this.organizationRepository.delete(organization);
    }

    @Override
    public ByteArrayInputStream loadCSV() {
        List<Organization> organizations = organizationRepository.findAll();
        return Organization.toCSV(organizations);
    }

    private void addRelatedData(OrganizationDTO organizationDTO) {
        organizationDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(organizationDTO.getId()));
        organizationDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(organizationDTO.getId()));
    }
}
