package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.organizationRepository = organizationRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<OrganizationDTO> getAllOrganizations() {
        List<OrganizationDTO> organizationDTOs = this.organizationRepository.findAll().stream().map(Organization::toDTO).toList();
        organizationDTOs.forEach(organizationDTO -> {
            organizationDTO.getAttributes().addAll(this.organizationRepository.getAttributes(organizationDTO.getId()).stream().map(Attribute::toDTO).toList());
            organizationDTO.getMetadata().addAll(this.organizationRepository.getMetadata(organizationDTO.getId()).stream().map(Metadata::toDTO).toList());
        });
        return organizationDTOs;
    }

    @Override
    public OrganizationDTO getOrganizationById(String id) {
        Organization organization = this.organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        OrganizationDTO organizationDTO = organization.toDTO();
        organizationDTO.getAttributes().addAll(this.organizationRepository.getAttributes(organizationDTO.getId()).stream().map(Attribute::toDTO).toList());
        organizationDTO.getMetadata().addAll(this.organizationRepository.getMetadata(organizationDTO.getId()).stream().map(Metadata::toDTO).toList());
        return organizationDTO;
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = this.organizationRepository.save(organizationDTO.toEntity());

        for (AttributeDTO attributeDTO : organizationDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(organization.getId()));
        }

        for (MetadataDTO metadataDTO : organizationDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(organization.getId()));
        }

        OrganizationDTO savedOrganizationDTO = this.organizationRepository.save(organization).toDTO();
        savedOrganizationDTO.getAttributes().addAll(this.organizationRepository.getAttributes(savedOrganizationDTO.getId()).stream().map(Attribute::toDTO).toList());
        savedOrganizationDTO.getMetadata().addAll(this.organizationRepository.getMetadata(savedOrganizationDTO.getId()).stream().map(Metadata::toDTO).toList());
        return savedOrganizationDTO;
    }

    @Override
    public OrganizationDTO updateOrganization(String id, OrganizationDTO organizationDTO) {
        Organization organization = this.organizationRepository.findById(id)
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
        organization.setParentOrganization(organizationDTO.getParentOrganization().toEntity());

        Organization updatedOrganization = this.organizationRepository.save(organization);
        return updatedOrganization.toDTO();
    }

    @Override
    public void deleteOrganization(String id) {
        Organization organization = this.organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        this.organizationRepository.deleteAttributes(organization.getId());
        this.organizationRepository.deleteMetadata(organization.getId());
        this.organizationRepository.delete(organization);
    }
}
