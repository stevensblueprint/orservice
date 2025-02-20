package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
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
        Organization organization = organizationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        return organization.toDTO();
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        if (organizationDTO.getId() == null || organizationDTO.getId().isEmpty()) {
            organizationDTO.setId(UUID.randomUUID().toString());
        }
        Organization organization = mapToEntity(organizationDTO);
        Organization saved = organizationRepository.save(organization);
        return saved.toDTO();
    }

    @Override
    public OrganizationDTO updateOrganization(String id, OrganizationDTO organizationDTO) {
        Organization existing = organizationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        existing.setName(organizationDTO.getName());
        existing.setAlternateName(organizationDTO.getAlternateName());
        existing.setDescription(organizationDTO.getDescription());
        existing.setEmail(organizationDTO.getEmail());
        existing.setWebsite(organizationDTO.getWebsite());
        existing.setTaxStatus(organizationDTO.getTaxStatus());
        existing.setTaxId(organizationDTO.getTaxId());
        existing.setYearIncorporated(organizationDTO.getYearIncorporated());
        existing.setLegalStatus(organizationDTO.getLegalStatus());
        existing.setLogo(organizationDTO.getLogo());
        existing.setUri(organizationDTO.getUri());
        // TODO: Update related collections as needed.
        Organization updated = organizationRepository.save(existing);
        return updated.toDTO();
    }

    @Override
    public void deleteOrganization(String id) {
        Organization existing = organizationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        organizationRepository.delete(existing);
    }

    private Organization mapToEntity(OrganizationDTO dto) {
        return Organization.builder()
            .id(dto.getId())
            .name(dto.getName())
            .alternateName(dto.getAlternateName())
            .description(dto.getDescription())
            .email(dto.getEmail())
            .website(dto.getWebsite())
            .taxStatus(dto.getTaxStatus())
            .taxId(dto.getTaxId())
            .yearIncorporated(dto.getYearIncorporated())
            .legalStatus(dto.getLegalStatus())
            .logo(dto.getLogo())
            .uri(dto.getUri())
            .build();
    }
}
