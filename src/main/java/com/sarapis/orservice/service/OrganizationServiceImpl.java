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
        List<OrganizationDTO> organizationDTOs = organizationRepository.findAll().stream().map(Organization::toDTO).toList();

        organizationDTOs.forEach(this::getRelatedData);

        return organizationDTOs;
    }

    @Override
    public OrganizationDTO getOrganizationById(String id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Organization not found."));
        OrganizationDTO organizationDTO = organization.toDTO();

        getRelatedData(organizationDTO);
        return organizationDTO;
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = organizationRepository.save(organizationDTO.toEntity());

        for (AttributeDTO attributeDTO : organizationDTO.getAttributes()) {
            attributeRepository.save(attributeDTO.toEntity(organization.getId()));
        }

        for (MetadataDTO metadataDTO : organizationDTO.getMetadata()) {
            metadataRepository.save(metadataDTO.toEntity(organization.getId()));
        }

        OrganizationDTO savedOrganizationDTO = organizationRepository.save(organization).toDTO();
        getRelatedData(savedOrganizationDTO);
        return savedOrganizationDTO;
    }

    @Override
    public OrganizationDTO updateOrganization(String id, OrganizationDTO organizationDTO) {
        Organization existingOrganization = organizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Organization not found."));
        existingOrganization.setName(organizationDTO.getName());
        existingOrganization.setAlternateName(organizationDTO.getAlternateName());
        existingOrganization.setDescription(organizationDTO.getDescription());
        existingOrganization.setEmail(organizationDTO.getEmail());
        existingOrganization.setWebsite(organizationDTO.getWebsite());
        existingOrganization.setTaxStatus(organizationDTO.getTaxStatus());
        existingOrganization.setTaxId(organizationDTO.getTaxId());
        existingOrganization.setYearIncorporated(organizationDTO.getYearIncorporated());
        existingOrganization.setLegalStatus(organizationDTO.getLegalStatus());
        existingOrganization.setLogo(organizationDTO.getLogo());
        existingOrganization.setUri(organizationDTO.getUri());
        existingOrganization.setParentOrganization(organizationDTO.getParentOrganization().toEntity());
        Organization updatedOrganization = organizationRepository.save(existingOrganization);
        return updatedOrganization.toDTO();
    }

    @Override
    public void deleteOrganization(String id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new RuntimeException("Organization not found."));

        List<Attribute> attributes = attributeRepository.findAll();
        List<Attribute> relatedAttributes = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), organization.getId())).toList();
        attributeRepository.deleteAll(relatedAttributes);

        List<Metadata> metadatas = metadataRepository.findAll();
        List<Metadata> relatedMetadatas = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), organization.getId())).toList();
        metadataRepository.deleteAll(relatedMetadatas);

        organizationRepository.delete(organization);
    }

    private void getRelatedData(OrganizationDTO organizationDTO) {
        List<Attribute> attributes = attributeRepository.findAll();
        List<AttributeDTO> relatedAttributeDTOs = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), organizationDTO.getId())).map(Attribute::toDTO).toList();

        List<Metadata> metadatas = metadataRepository.findAll();
        List<MetadataDTO> relatedMetadataDTOs = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), organizationDTO.getId())).map(Metadata::toDTO).toList();

        organizationDTO.getAttributes().addAll(relatedAttributeDTOs);
        organizationDTO.getMetadata().addAll(relatedMetadataDTOs);
    }
}
