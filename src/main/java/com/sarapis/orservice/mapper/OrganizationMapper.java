package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring",  uses = { OrganizationIdentifierMapper.class, UrlMapper.class })
public abstract class OrganizationMapper {

  @Autowired
  private OrganizationIdentifierMapper identifierMapper;

  @Autowired
  private UrlMapper urlMapper;

  @Autowired
  private FundingMapper fundingMapper;

  @Autowired
  private PhoneMapper phoneMapper;

  @Autowired
  private ProgramMapper programMapper;

  @Autowired
  private ContactMapper contactMapper;

  @Autowired
  private LocationMapper locationMapper;

  @Autowired
  private ServiceMapper serviceMapper;

  private static final Boolean shouldNotIncludeOrganization = false;

  public abstract Organization toEntity(OrganizationDTO.Request dto);
  public abstract OrganizationDTO.Response toResponseDTO(Organization entity);

  @AfterMapping
  protected void setRelations(@MappingTarget Organization organization) {
    if (organization.getAdditionalWebsites() != null) {
      organization.getAdditionalWebsites().forEach(website ->
          website.setOrganization(organization));
    }

    if (organization.getFunding() != null) {
      organization.getFunding().forEach(funding ->
          funding.setOrganization(organization));
    }

    if (organization.getPhones() != null) {
      organization.getPhones().forEach(phone ->
          phone.setOrganization(organization));
    }

    if (organization.getPrograms() != null) {
      organization.getPrograms().forEach(program ->
          program.setOrganization(organization));
    }

    if (organization.getContacts() != null) {
      organization.getContacts().forEach(contact ->
          contact.setOrganization(organization));
    }

    if (organization.getLocations() != null) {
      organization.getLocations().forEach(location ->
          location.setOrganization(organization));
    }

    if (organization.getOrganizationIdentifiers() != null) {
      organization.getOrganizationIdentifiers().forEach(identifier ->
          identifier.setOrganization(organization));
    }
  }

  public OrganizationDTO.Response toResponseDTO(Organization entity, MetadataService metadataService, Boolean fullService) {
    OrganizationDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
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

  protected void enrichMetadata(Organization organization, @MappingTarget OrganizationDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            organization.getId(), ORGANIZATION_RESOURCE_TYPE
        )
    );
  }
}
