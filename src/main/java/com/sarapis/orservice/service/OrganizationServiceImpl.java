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
import java.util.Objects;

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
        organizationDTOs.forEach(this::getRelatedData);
        return organizationDTOs;
    }

    @Override
    public OrganizationDTO getOrganizationById(String id) {
        Organization organization = this.organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        OrganizationDTO organizationDTO = organization.toDTO();
        getRelatedData(organizationDTO);
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
        getRelatedData(savedOrganizationDTO);
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

        List<Attribute> attributes = this.attributeRepository.findAll();
        List<Attribute> relatedAttributes = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), organization.getId())).toList();
        this.attributeRepository.deleteAll(relatedAttributes);

        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<Metadata> relatedMetadatas = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), organization.getId())).toList();
        this.metadataRepository.deleteAll(relatedMetadatas);

        this.organizationRepository.delete(organization);
    }

    private void getRelatedData(OrganizationDTO organizationDTO) {
        List<Attribute> attributes = this.attributeRepository.findAll();
        List<AttributeDTO> relatedAttributeDTOs = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), organizationDTO.getId())).map(Attribute::toDTO).toList();

        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<MetadataDTO> relatedMetadataDTOs = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), organizationDTO.getId())).map(Metadata::toDTO).toList();

        organizationDTO.getAttributes().addAll(relatedAttributeDTOs);
        organizationDTO.getMetadata().addAll(relatedMetadataDTOs);
    }
}
