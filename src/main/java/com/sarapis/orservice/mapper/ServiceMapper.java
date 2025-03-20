package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.COST_OPTION_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.RequiredDocumentDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ServiceMapper {
  @Autowired
  private PhoneMapper phoneMapper;

  @Autowired
  private ScheduleMapper scheduleMapper;

  @Autowired
  private ServiceAreaMapper serviceAreaMapper;

  @Autowired
  private ServiceAtLocationMapper serviceAtLocationMapper;

  @Autowired
  private LanguageMapper languageMapper;

  @Autowired
  private CostOptionMapper costOptionMapper;

  @Autowired
  private RequiredDocumentMapper requiredDocumentMapper;

  @Autowired
  private ContactMapper contactMapper;

  @Autowired
  private FundingMapper fundingMapper;

  @Autowired
  private UrlMapper urlMapper;

  public abstract Service toEntity(ServiceDTO.Request dto);
  public abstract  Response toResponseDTO(Service entity);

  @AfterMapping
  protected void setRelations(@MappingTarget Service service) {
    if (service.getPhones() != null) {
      service.getPhones().forEach(phone ->
          phone.setService(service));
    }

    if (service.getSchedules() != null) {
      service.getSchedules().forEach(schedule ->
          schedule.setService(service));
    }

    if (service.getServiceAreas() != null) {
      service.getServiceAreas().forEach(serviceArea ->
          serviceArea.setService(service));
    }

    if (service.getServiceAtLocations() != null) {
      service.getServiceAtLocations().forEach(serviceAtLocation ->
          serviceAtLocation.setService(service));
    }

    if (service.getLanguages() != null) {
      service.getLanguages().forEach(language ->
          language.setService(service));
    }

    if (service.getCostOptions() != null) {
      service.getCostOptions().forEach(costOption ->
          costOption.setService(service));
    }

    if (service.getRequiredDocuments()!= null) {
      service.getRequiredDocuments().forEach(requiredDocument ->
          requiredDocument.setService(service));
    }

    if (service.getContacts()!= null) {
      service.getContacts().forEach(contact ->
          contact.setService(service));
    }

    if (service.getFunding() != null) {
      service.getFunding().forEach(funding ->
          funding.setService(service));
    }

    if (service.getAdditionalUrls() != null) {
      service.getAdditionalUrls().forEach(url ->
          url.setService(service));
    }
  }

  public Service toEntity(ServiceDTO.Request dto, OrganizationRepository organizationRepository) {
    Service service = toEntity(dto);
    if (service.getOrganization().getId() != null) {
      service.setOrganization(organizationRepository.findById(service.getOrganization().getId()).orElseThrow(
          () -> new IllegalArgumentException("Organization not found for service with ID: " + service.getOrganization().getId())
      ));
    }
    return service;
  }

  public ServiceDTO.Response toResponseDTO(Service entity, MetadataService metadataService) {
    ServiceDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    if (entity.getPhones()!= null) {
      List<PhoneDTO.Response> enrichedPhones =
          entity.getPhones().stream()
             .map(phone -> phoneMapper.toResponseDTO(phone, metadataService)).toList();
      response.setPhones(enrichedPhones);
    }

    if (entity.getSchedules() != null) {
      List<ScheduleDTO.Response> enrichedSchedules =
          entity.getSchedules().stream()
             .map(schedule -> scheduleMapper.toResponseDTO(schedule, metadataService)).toList();
      response.setSchedules(enrichedSchedules);
    }

    if (entity.getServiceAreas() != null) {
      List<ServiceAreaDTO.Response> enrichedServiceAreas =
          entity.getServiceAreas().stream()
             .map(serviceArea -> serviceAreaMapper.toResponseDTO(serviceArea, metadataService)).toList();
      response.setServiceAreas(enrichedServiceAreas);
    }

    if (entity.getLanguages() != null) {
      List<LanguageDTO.Response> enrichedLanguages =
          entity.getLanguages().stream()
             .map(language -> languageMapper.toResponseDTO(language, metadataService)).toList();
      response.setLanguages(enrichedLanguages);
    }

    if (entity.getCostOptions()!= null) {
      List<CostOptionDTO.Response> enrichedCostOptions =
          entity.getCostOptions().stream()
             .map(costOption -> costOptionMapper.toResponseDTO(costOption, metadataService)).toList();
      response.setCostOptions(enrichedCostOptions);
    }

    if (entity.getRequiredDocuments() != null) {
      List<RequiredDocumentDTO.Response> enrichedRequiredDocuments =
          entity.getRequiredDocuments().stream()
             .map(requiredDocument -> requiredDocumentMapper.toResponseDTO(requiredDocument, metadataService)).toList();
      response.setRequiredDocuments(enrichedRequiredDocuments);
    }

    if (entity.getContacts() != null) {
      List<ContactDTO.Response> enrichedContacts =
          entity.getContacts().stream()
             .map(contact -> contactMapper.toResponseDTO(contact, metadataService)).toList();
      response.setContacts(enrichedContacts);
    }

    if (entity.getFunding() != null) {
      List<FundingDTO.Response> enrichedFunding =
          entity.getFunding().stream()
             .map(funding -> fundingMapper.toResponseDTO(funding, metadataService)).toList();
      response.setFunding(enrichedFunding);
    }

    if (entity.getAdditionalUrls() != null) {
      List<UrlDTO.Response> enrichedUrls =
          entity.getAdditionalUrls().stream()
             .map(url -> urlMapper.toResponseDTO(url, metadataService)).toList();
      response.setAdditionalUrls(enrichedUrls);
    }

    return response;
  }

  private void enrichMetadata(Service entity, ServiceDTO.Response response, MetadataService metadataService) {
    response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(
        entity.getId(), COST_OPTION_RESOURCE_TYPE
    ));
  }
}
