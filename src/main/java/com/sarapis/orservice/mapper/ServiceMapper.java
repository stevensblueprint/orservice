package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.dto.ServiceDTO.Summary;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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

  @Autowired
  private OrganizationRepository organizationRepository;

  public abstract Service toEntity(ServiceDTO.Request dto);
  public abstract Response toResponseDTO(Service entity);
  public abstract Summary toSummaryDTO(Service entity);

  @AfterMapping
  protected void setRelations(@MappingTarget Service service) {
    if (service.getPhones() != null) {
      service.getPhones().forEach(phone -> phone.setService(service));
    }
    if (service.getSchedules() != null) {
      service.getSchedules().forEach(schedule -> schedule.setService(service));
    }
    if (service.getServiceAreas() != null) {
      service.getServiceAreas().forEach(serviceArea -> serviceArea.setService(service));
    }
    if (service.getServiceAtLocations() != null) {
      service.getServiceAtLocations().forEach(serviceAtLocation -> serviceAtLocation.setService(service));
    }
    if (service.getLanguages() != null) {
      service.getLanguages().forEach(language -> language.setService(service));
    }
    if (service.getCostOptions() != null) {
      service.getCostOptions().forEach(costOption -> costOption.setService(service));
    }
    if (service.getRequiredDocuments() != null) {
      service.getRequiredDocuments().forEach(requiredDocument -> requiredDocument.setService(service));
    }
    if (service.getContacts() != null) {
      service.getContacts().forEach(contact -> contact.setService(service));
    }
    if (service.getFunding() != null) {
      service.getFunding().forEach(funding -> funding.setService(service));
    }
    if (service.getAdditionalUrls() != null) {
      service.getAdditionalUrls().forEach(url -> url.setService(service));
    }
  }

  @AfterMapping
  public Service toEntity(@MappingTarget Service service) {
    if (service.getOrganization().getId() != null) {
      service.setOrganization(organizationRepository.findById(service.getOrganization().getId()).orElseThrow(
          () -> new IllegalArgumentException("Organization not found for service with ID: " + service.getOrganization().getId())
      ));
    }
    return service;
  }

  public ServiceDTO.Response toResponseDTO(Service entity, MetadataService metadataService) {
    Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);

    enrichList(entity.getPhones(), response::setPhones, (phone, meta) -> phoneMapper.toResponseDTO(phone, meta), metadataService);
    enrichList(entity.getSchedules(), response::setSchedules, (schedule, meta) -> scheduleMapper.toResponseDTO(schedule, meta), metadataService);
    enrichList(entity.getServiceAreas(), response::setServiceAreas, (area, meta) -> serviceAreaMapper.toResponseDTO(area, meta), metadataService);
    enrichList(entity.getLanguages(), response::setLanguages, (lang, meta) -> languageMapper.toResponseDTO(lang, meta), metadataService);
    enrichList(entity.getCostOptions(), response::setCostOptions, (cost, meta) -> costOptionMapper.toResponseDTO(cost, meta), metadataService);
    enrichList(entity.getRequiredDocuments(), response::setRequiredDocuments, (doc, meta) -> requiredDocumentMapper.toResponseDTO(doc, meta), metadataService);
    enrichList(entity.getContacts(), response::setContacts, (contact, meta) -> contactMapper.toResponseDTO(contact, meta), metadataService);
    enrichList(entity.getFunding(), response::setFunding, (funding, meta) -> fundingMapper.toResponseDTO(funding, meta), metadataService);
    enrichList(entity.getAdditionalUrls(), response::setAdditionalUrls, (url, meta) -> urlMapper.toResponseDTO(url, meta), metadataService);
    enrichList(entity.getServiceAtLocations(), response::setServiceAtLocations, (sal, meta) -> serviceAtLocationMapper.toResponseDTO(sal, meta), metadataService);

    return response;
  }

  public ServiceDTO.Summary toSummaryDTO(Service entity, MetadataService metadataService) {
    Summary summary = toSummaryDTO(entity);
    enrichMetadata(entity, summary, metadataService);

    enrichList(entity.getPhones(), summary::setPhones, (phone, meta) -> phoneMapper.toResponseDTO(phone, meta), metadataService);
    enrichList(entity.getSchedules(), summary::setSchedules, (schedule, meta) -> scheduleMapper.toResponseDTO(schedule, meta), metadataService);
    enrichList(entity.getServiceAreas(), summary::setServiceAreas, (area, meta) -> serviceAreaMapper.toResponseDTO(area, meta), metadataService);
    enrichList(entity.getLanguages(), summary::setLanguages, (lang, meta) -> languageMapper.toResponseDTO(lang, meta), metadataService);
    enrichList(entity.getCostOptions(), summary::setCostOptions, (cost, meta) -> costOptionMapper.toResponseDTO(cost, meta), metadataService);
    enrichList(entity.getRequiredDocuments(), summary::setRequiredDocuments, (doc, meta) -> requiredDocumentMapper.toResponseDTO(doc, meta), metadataService);
    enrichList(entity.getContacts(), summary::setContacts, (contact, meta) -> contactMapper.toResponseDTO(contact, meta), metadataService);
    enrichList(entity.getFunding(), summary::setFunding, (funding, meta) -> fundingMapper.toResponseDTO(funding, meta), metadataService);
    enrichList(entity.getAdditionalUrls(), summary::setAdditionalUrls, (url, meta) -> urlMapper.toResponseDTO(url, meta), metadataService);
    enrichList(entity.getServiceAtLocations(), summary::setServiceAtLocations, (sal, meta) -> serviceAtLocationMapper.toResponseDTO(sal, meta), metadataService);

    return summary;
  }

  /**
   * Helper method to enrich a list if it is not null.
   *
   * @param sourceList the source list of entities
   * @param setter a consumer to set the resulting list on the DTO
   * @param mapper a function that converts an entity and metadataService to a DTO object
   * @param metadataService the metadata service
   * @param <T> the type of the source entity
   * @param <R> the type of the response DTO
   */
  private <T, R> void enrichList(List<T> sourceList, Consumer<List<R>> setter,
      BiFunction<T, MetadataService, R> mapper, MetadataService metadataService) {
    if (sourceList != null) {
      List<R> enriched = sourceList.stream()
          .map(item -> mapper.apply(item, metadataService))
          .collect(Collectors.toList());
      setter.accept(enriched);
    }
  }

  private void enrichMetadata(Service entity, ServiceDTO.Response response, MetadataService metadataService) {
    response.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(
        entity.getId(), SERVICE_RESOURCE_TYPE));
  }

  private void enrichMetadata(Service entity, ServiceDTO.Summary summary, MetadataService metadataService) {
    summary.setMetadata(metadataService.getMetadataByResourceIdAndResourceType(
        entity.getId(), SERVICE_RESOURCE_TYPE));
  }
}
