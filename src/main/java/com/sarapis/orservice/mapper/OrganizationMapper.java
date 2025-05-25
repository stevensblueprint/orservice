package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.model.Funding;
import com.sarapis.orservice.model.Location;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.model.OrganizationIdentifier;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.model.Program;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.ContactRepository;
import com.sarapis.orservice.repository.FundingRepository;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.OrganizationIdentifierRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.repository.ProgramRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.UrlRepository;
import com.sarapis.orservice.service.MetadataService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring",  uses = { OrganizationIdentifierMapper.class, UrlMapper.class })
public abstract class OrganizationMapper {

  @Autowired
  private OrganizationIdentifierMapper identifierMapper;
  @Autowired
  private OrganizationIdentifierRepository organizationIdentifierRepository;

  @Autowired
  private UrlMapper urlMapper;
  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private FundingMapper fundingMapper;
  @Autowired
  private FundingRepository fundingRepository;

  @Autowired
  private PhoneMapper phoneMapper;
  @Autowired
  private PhoneRepository phoneRepository;

  @Autowired
  private ProgramMapper programMapper;
  @Autowired
  private ProgramRepository programRepository;

  @Autowired
  private ContactMapper contactMapper;
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private LocationMapper locationMapper;
  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ServiceMapper serviceMapper;
  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Mapping(target = "parentOrganization.id", source = "parentOrganizationId")
  public abstract Organization toEntity(OrganizationDTO.Request dto);
  @Mapping(target = "parentOrganizationId", source = "parentOrganization.id")
  public abstract OrganizationDTO.Response toResponseDTO(Organization entity);

  @AfterMapping
  public Organization toEntity(@MappingTarget Organization organization) {
    Optional.ofNullable(organization.getParentOrganization())
        .map(Organization::getId)
        .ifPresent(orgId -> {
          Organization org = organizationRepository.findById(orgId)
              .orElseThrow(() -> new EntityNotFoundException("Organization not found with ID " + orgId));
          organization.setParentOrganization(org);
        });
    return organization;
  }

  @AfterMapping
  protected void setRelations(@MappingTarget Organization organization) {
    if (organization.getAdditionalWebsites() != null) {
      List<Url> managedUrls = organization.getAdditionalWebsites().stream()
          .map(url -> {
            if (url.getId()!= null) {
              return urlRepository.findById(url.getId())
                 .orElse(url);
            }
            return url;
          })
          .peek(url -> url.setOrganization(organization)).toList();
      organization.setAdditionalWebsites(managedUrls);
    }

    if (organization.getFunding() != null) {
      List<Funding> managedFundings = organization.getFunding().stream()
          .map(funding -> {
            if (funding.getId()!= null) {
              return fundingRepository.findById(funding.getId())
                 .orElse(funding);
            }
            return funding;
          })
          .peek(funding -> funding.setOrganization(organization)).toList();
      organization.setFunding(managedFundings);
    }

    if (organization.getPhones() != null) {
      List<Phone> managedPhones = organization.getPhones().stream()
          .map(phone -> {
            if (phone.getId()!= null) {
              return phoneRepository.findById(phone.getId())
                 .orElse(phone);
            }
            return phone;
          })
          .peek(phone -> phone.setOrganization(organization)).toList();
      organization.setPhones(managedPhones);
    }

    if (organization.getPrograms() != null) {
      List<Program> managedPrograms = organization.getPrograms().stream()
          .map(program -> {
            if (program.getId()!= null) {
              return programRepository.findById(program.getId())
                 .orElse(program);
            }
            return program;
          })
          .peek(program -> program.setOrganization(organization)).toList();
      organization.setPrograms(managedPrograms);
    }

    if (organization.getContacts() != null) {
      List<Contact> managedContacts = organization.getContacts().stream()
          .map(contact -> {
            if (contact.getId()!= null) {
              return contactRepository.findById(contact.getId())
                 .orElse(contact);
            }
            return contact;
          })
          .peek(contact -> contact.setOrganization(organization)).toList();
      organization.setContacts(managedContacts);
    }

    if (organization.getLocations() != null) {
      List<Location> managedLocations = organization.getLocations().stream()
          .map(location -> {
            if (location.getId()!= null) {
              return locationRepository.findById(location.getId())
                 .orElse(location);
            }
            return location;
          })
          .peek(location -> location.setOrganization(organization)).toList();
      organization.setLocations(managedLocations);
    }

    if (organization.getOrganizationIdentifiers() != null) {
      List<OrganizationIdentifier> managedOrganizationIdentifiers = organization
          .getOrganizationIdentifiers()
          .stream()
          .map(identifier -> {
            if (identifier.getId()!= null) {
              return organizationIdentifierRepository.findById(identifier.getId())
                 .orElse(identifier);
            }
            return identifier;
          })
          .peek(identifier -> identifier.setOrganization(organization)).toList();
      organization.setOrganizationIdentifiers(managedOrganizationIdentifiers);
    }
  }

  public OrganizationDTO.Response toResponseDTO(Organization entity, MetadataService metadataService, Boolean fullService) {
    OrganizationDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Organization::getId,
        OrganizationDTO.Response::setMetadata,
        ORGANIZATION_RESOURCE_TYPE,
        metadataService
    );
    if (entity.getOrganizationIdentifiers() != null) {
      List<OrganizationIdentifierDTO.Response> enrichedIdentifiers =
          entity.getOrganizationIdentifiers().stream()
              .map(identifier -> identifierMapper.toResponseDTO(identifier, metadataService))
              .collect(Collectors.toList());
      response.setOrganizationIdentifiers(enrichedIdentifiers);
    }

    if (entity.getAdditionalWebsites() != null) {
      List<UrlDTO.Response> enrichedAdditionalWebsites =
          entity.getAdditionalWebsites().stream()
              .map(website -> urlMapper.toResponseDTO(website, metadataService))
              .collect(Collectors.toList());
      response.setAdditionalWebsites(enrichedAdditionalWebsites);
    }

    if (entity.getFunding() != null) {
      List<FundingDTO.Response> enrichedFunding =
          entity.getFunding().stream()
              .map(funding -> fundingMapper.toResponseDTO(funding, metadataService))
              .collect(Collectors.toList());
      response.setFunding(enrichedFunding);
    }

    if (entity.getPrograms() != null) {
      List<ProgramDTO.Response> enrichedPrograms =
          entity.getPrograms().stream()
              .map(program -> programMapper.toResponseDTO(program, metadataService))
              .collect(Collectors.toList());
      response.setPrograms(enrichedPrograms);
    }

    if (entity.getLocations() != null) {
      List<LocationDTO.Response> enrichedLocations =
          entity.getLocations().stream()
              .map(location -> locationMapper.toResponseDTO(location, metadataService))
              .collect(Collectors.toList());
      response.setLocations(enrichedLocations);
    }

    if (entity.getContacts() != null) {
      List<ContactDTO.Response> enrichedContacts =
          entity.getContacts().stream()
             .map(contact -> contactMapper.toResponseDTO(contact, metadataService))
             .collect(Collectors.toList());
      response.setContacts(enrichedContacts);
    }

    if (entity.getPhones() != null) {
      List<PhoneDTO.Response> enrichedPhones =
          entity.getPhones().stream()
             .map(phone -> phoneMapper.toResponseDTO(phone, metadataService))
             .collect(Collectors.toList());
      response.setPhones(enrichedPhones);
    }

    if (entity.getServices() != null) {
      List<ServiceDTO.Summary> enrichedServices =
          entity.getServices().stream()
             .map(service -> fullService ? serviceMapper.toSummaryDTO(service, metadataService) : serviceMapper.toSummaryDTOShort(service))
             .collect(Collectors.toList());
      response.setServices(enrichedServices);
    }

    return response;
  }
}
