package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationIdentifierDTO;
import com.sarapis.orservice.entity.*;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
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
                .orElseThrow(() -> new RuntimeException("Organization not found."));
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
                    .orElseThrow(() -> new RuntimeException("Parent organization not found."));
            parentOrganization.getChildOrganizations().add(createdOrganization);
            this.organizationRepository.save(parentOrganization);
            createdOrganization.setParentOrganization(parentOrganization);
        }

        createdOrganization.setAdditionalWebsites(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getAdditionalWebsites()) {
            Url url = this.urlRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Url not found."));
            url.setOrganization(createdOrganization);
            this.urlRepository.save(url);
            createdOrganization.getAdditionalWebsites().add(url);
        }

        createdOrganization.setFunding(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getFundings()) {
            Funding funding = this.fundingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Funding not found."));
            funding.setOrganization(createdOrganization);
            this.fundingRepository.save(funding);
            createdOrganization.getFunding().add(funding);
        }

        createdOrganization.setLocations(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getLocations()) {
            Location location = this.locationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Location not found."));
            location.setOrganization(createdOrganization);
            this.locationRepository.save(location);
            createdOrganization.getLocations().add(location);
        }

        createdOrganization.setPhones(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getPhones()) {
            Phone phone = this.phoneRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Phone not found."));
            phone.setOrganization(createdOrganization);
            this.phoneRepository.save(phone);
            createdOrganization.getPhones().add(phone);
        }

        createdOrganization.setContacts(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getContacts()) {
            Contact contact = this.contactRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contact not found."));
            contact.setOrganization(createdOrganization);
            this.contactRepository.save(contact);
            createdOrganization.getContacts().add(contact);
        }

        createdOrganization.setPrograms(new ArrayList<>());
        for (String id : upsertOrganizationDTO.getPrograms()) {
            Program program = this.programRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Program not found."));
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
    public OrganizationDTO updateOrganization(String organizationId, UpsertOrganizationDTO upsertOrganizationDTO) {
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found."));
        Organization updatedOrganization = upsertOrganizationDTO.merge(organization);
        updatedOrganization.setId(organizationId);

        if (upsertOrganizationDTO.getParentOrganizationId() != null) {
            Organization parentOrganization = this.organizationRepository
                    .findById(upsertOrganizationDTO.getParentOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Parent organization not found."));
            parentOrganization.getChildOrganizations().add(updatedOrganization);
            this.organizationRepository.save(parentOrganization);
            if (organization.getParentOrganization() != null) {
                organization.getParentOrganization().getChildOrganizations().remove(organization);
            }
            updatedOrganization.setParentOrganization(parentOrganization);
        }

        if (upsertOrganizationDTO.getAdditionalWebsites() != null && !upsertOrganizationDTO.getAdditionalWebsites().isEmpty()) {
            for (Url url : organization.getAdditionalWebsites()) {
                url.setOrganization(null);
                this.urlRepository.save(url);
            }
            updatedOrganization.setAdditionalWebsites(new ArrayList<>());
            for (String id : upsertOrganizationDTO.getAdditionalWebsites()) {
                Url url = this.urlRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Url not found."));
                url.setOrganization(updatedOrganization);
                this.urlRepository.save(url);
                updatedOrganization.getAdditionalWebsites().add(url);
            }
        }

        if (upsertOrganizationDTO.getFundings() != null && !upsertOrganizationDTO.getFundings().isEmpty()) {
            for (Funding funding : organization.getFunding()) {
                funding.setOrganization(null);
                this.fundingRepository.save(funding);
            }
            updatedOrganization.setFunding(new ArrayList<>());
            for (String id : upsertOrganizationDTO.getFundings()) {
                Funding funding = this.fundingRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Funding not found."));
                funding.setOrganization(updatedOrganization);
                this.fundingRepository.save(funding);
                updatedOrganization.getFunding().add(funding);
            }
        }

        if (upsertOrganizationDTO.getLocations() != null && !upsertOrganizationDTO.getLocations().isEmpty()) {
            for (Location location : organization.getLocations()) {
                location.setOrganization(null);
                this.locationRepository.save(location);
            }
            updatedOrganization.setLocations(new ArrayList<>());
            for (String id : upsertOrganizationDTO.getLocations()) {
                Location location = this.locationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Location not found."));
                location.setOrganization(updatedOrganization);
                this.locationRepository.save(location);
                updatedOrganization.getLocations().add(location);
            }
        }

        if (upsertOrganizationDTO.getPhones() != null && !upsertOrganizationDTO.getPhones().isEmpty()) {
            for (Phone phone : organization.getPhones()) {
                phone.setOrganization(null);
                this.phoneRepository.save(phone);
            }
            updatedOrganization.setPhones(new ArrayList<>());
            for (String id : upsertOrganizationDTO.getPhones()) {
                Phone phone = this.phoneRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Phone not found."));
                phone.setOrganization(updatedOrganization);
                this.phoneRepository.save(phone);
                updatedOrganization.getPhones().add(phone);
            }
        }

        if (upsertOrganizationDTO.getContacts() != null && !upsertOrganizationDTO.getContacts().isEmpty()) {
            for (Contact contact : organization.getContacts()) {
                contact.setOrganization(null);
                this.contactRepository.save(contact);
            }
            updatedOrganization.setContacts(new ArrayList<>());
            for (String id : upsertOrganizationDTO.getContacts()) {
                Contact contact = this.contactRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Contact not found."));
                contact.setOrganization(updatedOrganization);
                this.contactRepository.save(contact);
                updatedOrganization.getContacts().add(contact);
            }
        }

        if (upsertOrganizationDTO.getPrograms() != null && !upsertOrganizationDTO.getPrograms().isEmpty()) {
            for (Program program : organization.getPrograms()) {
                program.setOrganization(null);
                this.programRepository.save(program);
            }
            updatedOrganization.setPrograms(new ArrayList<>());
            for (String id : upsertOrganizationDTO.getPrograms()) {
                Program program = this.programRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Program not found."));
                program.setOrganization(updatedOrganization);
                this.programRepository.save(program);
                updatedOrganization.getPrograms().add(program);
            }
        }

        if (upsertOrganizationDTO.getOrganizationIdentifiers() != null && !upsertOrganizationDTO.getOrganizationIdentifiers().isEmpty()) {
            for (OrganizationIdentifier organizationIdentifier : organization.getOrganizationIdentifiers()) {
                organizationIdentifier.setOrganization(null);
                this.organizationIdentifierRepository.save(organizationIdentifier);
            }
            updatedOrganization.setOrganizationIdentifiers(new ArrayList<>());
            for (UpsertOrganizationIdentifierDTO dto : upsertOrganizationDTO.getOrganizationIdentifiers()) {
                OrganizationIdentifier organizationIdentifier = dto.create();
                organizationIdentifier.setOrganization(updatedOrganization);
                OrganizationIdentifier createdOrganizationIdentifier = this.organizationIdentifierRepository.save(organizationIdentifier);
                updatedOrganization.getOrganizationIdentifiers().add(createdOrganizationIdentifier);
            }
        }

        this.organizationRepository.save(updatedOrganization);
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
