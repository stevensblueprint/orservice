package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationIdentifierDTO;
import com.sarapis.orservice.entity.*;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.exception.ResourceNotFoundException;
import com.sarapis.orservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UrlRepository urlRepository;
    private final FundingRepository fundingRepository;
    private final AttributeService attributeService;
    private final LocationRepository locationRepository;
    private final PhoneRepository phoneRepository;
    private final ContactRepository contactRepository;
    private final ProgramRepository programRepository;
    private final OrganizationIdentifierRepository organizationIdentifierRepository;
    private final MetadataService metadataService;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   UrlRepository urlRepository,
                                   FundingRepository fundingRepository,
                                   AttributeService attributeService,
                                   LocationRepository locationRepository,
                                   PhoneRepository phoneRepository,
                                   ContactRepository contactRepository,
                                   ProgramRepository programRepository,
                                   OrganizationIdentifierRepository organizationIdentifierRepository,
                                   MetadataService metadataService) {
        this.organizationRepository = organizationRepository;
        this.urlRepository = urlRepository;
        this.fundingRepository = fundingRepository;
        this.attributeService = attributeService;
        this.locationRepository = locationRepository;
        this.phoneRepository = phoneRepository;
        this.contactRepository = contactRepository;
        this.programRepository = programRepository;
        this.organizationIdentifierRepository = organizationIdentifierRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));
        OrganizationDTO organizationDTO = organization.toDTO();
        this.addRelatedData(organizationDTO);
        return organizationDTO;
    }

    @Override
    public OrganizationDTO createOrganization(UpsertOrganizationDTO upsertOrganizationDTO) {
        Organization createdOrganization = this.organizationRepository.save(upsertOrganizationDTO.create());

        if (upsertOrganizationDTO.getParentOrganizationId() != null) {
            Organization parentOrganization = this.organizationRepository
                    .findById(upsertOrganizationDTO.getParentOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent organization not found."));
            createdOrganization.setParentOrganization(parentOrganization);
        }

        createdOrganization.setAdditionalWebsites(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getAdditionalWebsites()) {
            Url url = this.urlRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Url not found."));
            url.setOrganization(createdOrganization);
            this.urlRepository.save(url);
            createdOrganization.getAdditionalWebsites().add(url);
        }

        createdOrganization.setFunding(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getFundings()) {
            Funding funding = this.fundingRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Funding not found."));
            funding.setOrganization(createdOrganization);
            this.fundingRepository.save(funding);
            createdOrganization.getFunding().add(funding);
        }

        createdOrganization.setLocations(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getLocations()) {
            Location location = this.locationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found."));
            location.setOrganization(createdOrganization);
            this.locationRepository.save(location);
            createdOrganization.getLocations().add(location);
        }

        createdOrganization.setPhones(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getPhones()) {
            Phone phone = this.phoneRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Phone not found."));
            phone.setOrganization(createdOrganization);
            this.phoneRepository.save(phone);
            createdOrganization.getPhones().add(phone);
        }

        createdOrganization.setContacts(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getContacts()) {
            Contact contact = this.contactRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Contact not found."));
            contact.setOrganization(createdOrganization);
            this.contactRepository.save(contact);
            createdOrganization.getContacts().add(contact);
        }

        createdOrganization.setPrograms(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getPrograms()) {
            Program program = this.programRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Program not found."));
            program.setOrganization(createdOrganization);
            this.programRepository.save(program);
            createdOrganization.getPrograms().add(program);
        }

        createdOrganization.setOrganizationIdentifiers(new ArrayList<>());
        for (UpsertOrganizationIdentifierDTO dto : upsertOrganizationDTO.getOrganizationIdentifiers()) {
            OrganizationIdentifier organizationIdentifier = dto.create();
            organizationIdentifier.setOrganization(createdOrganization);
            OrganizationIdentifier createdOrganizationIdentifier = this.organizationIdentifierRepository.save(organizationIdentifier);
            createdOrganization.getOrganizationIdentifiers().add(createdOrganizationIdentifier);
        }

        this.organizationRepository.save(createdOrganization);
        return this.getOrganizationById(createdOrganization.getId());
    }

    @Override
    public OrganizationDTO updateOrganization(String organizationId, OrganizationDTO organizationDTO) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));

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
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found."));
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
