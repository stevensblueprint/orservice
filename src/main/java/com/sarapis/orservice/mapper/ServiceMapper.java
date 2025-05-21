package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.dto.ServiceDTO.Response;
import com.sarapis.orservice.dto.ServiceDTO.Summary;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.model.CostOption;
import com.sarapis.orservice.model.Funding;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.model.RequiredDocument;
import com.sarapis.orservice.model.Schedule;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.model.ServiceArea;
import com.sarapis.orservice.model.ServiceAtLocation;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.ContactRepository;
import com.sarapis.orservice.repository.CostOptionRepository;
import com.sarapis.orservice.repository.FundingRepository;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.repository.ProgramRepository;
import com.sarapis.orservice.repository.RequiredDocumentRepository;
import com.sarapis.orservice.repository.ScheduleRepository;
import com.sarapis.orservice.repository.ServiceAreaRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.UrlRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ServiceMapper {
  @Autowired
  private PhoneMapper phoneMapper;
  @Autowired
  private PhoneRepository phoneRepository;

  @Autowired
  private ScheduleMapper scheduleMapper;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private ServiceAreaMapper serviceAreaMapper;
  @Autowired
  private ServiceAreaRepository serviceAreaRepository;

  @Autowired
  private ServiceAtLocationMapper serviceAtLocationMapper;
  @Autowired
  private ServiceAtLocationRepository serviceAtLocationRepository;

  @Autowired
  private LanguageMapper languageMapper;
  @Autowired
  private LanguageRepository languageRepository;

  @Autowired
  private CostOptionMapper costOptionMapper;
  @Autowired
  private CostOptionRepository costOptionRepository;

  @Autowired
  private RequiredDocumentMapper requiredDocumentMapper;
  @Autowired
  private RequiredDocumentRepository requiredDocumentRepository;

  @Autowired
  private ContactMapper contactMapper;
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private FundingMapper fundingMapper;
  @Autowired
  private FundingRepository fundingRepository;

  @Autowired
  private UrlMapper urlMapper;
  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private ProgramRepository programRepository;

  public abstract Service toEntity(ServiceDTO.Request dto);
  public abstract Response toResponseDTO(Service entity);
  public abstract Summary toSummaryDTO(Service entity);

  @Named("toSummaryDTOShort")
  @Mapping(target = "funding", ignore = true)
  @Mapping(target = "costOptions", ignore = true)
  @Mapping(target = "additionalUrls", ignore = true)
  @Mapping(target = "program", ignore = true)
  @Mapping(target = "requiredDocuments", ignore = true)
  @Mapping(target = "contacts", ignore = true)
  @Mapping(target = "phones", ignore = true)
  @Mapping(target = "schedules", ignore = true)
  @Mapping(target = "serviceAreas", ignore = true)
  @Mapping(target = "languages", ignore = true)
  @Mapping(target = "metadata", ignore = true)
  public abstract Summary toSummaryDTOShort(Service entity);

  @AfterMapping
  protected void setRelations(@MappingTarget Service service) {
    if (service.getPhones() != null) {
      List<Phone> managedPhones = service.getPhones().stream()
          .map(phone -> {
            if (phone.getId() != null) {
              return phoneRepository.findById(phone.getId())
                 .orElse(phone);
            }
            return phone;
          })
          .peek(phone -> phone.setService(service)).toList();
      service.setPhones(managedPhones);
    }

    if (service.getSchedules() != null) {
      List<Schedule> managedSchedules = service.getSchedules().stream()
          .map(schedule -> {
            if (schedule.getId() != null) {
              return scheduleRepository.findById(schedule.getId())
                 .orElse(schedule);
            }
            return schedule;
          })
          .peek(schedule -> schedule.setService(service)).toList();
      service.setSchedules(managedSchedules);
    }

    if (service.getServiceAreas() != null) {
      List<ServiceArea> managedServiceAreas = service.getServiceAreas().stream()
          .map(serviceArea -> {
            if (serviceArea.getId() != null) {
              return serviceAreaRepository.findById(serviceArea.getId())
                 .orElse(serviceArea);
            }
            return serviceArea;
          })
          .peek(serviceArea -> serviceArea.setService(service)).toList();
      service.setServiceAreas(managedServiceAreas);
    }

    if (service.getServiceAtLocations() != null) {
      List<ServiceAtLocation> managedServiceAtLocations = service.getServiceAtLocations().stream()
          .map(serviceAtLocation -> {
            if (serviceAtLocation.getId()!= null) {
              return serviceAtLocationRepository.findById(serviceAtLocation.getId())
                 .orElse(serviceAtLocation);
            }
            return serviceAtLocation;
          })
          .peek(serviceAtLocation -> serviceAtLocation.setService(service)).toList();
      service.setServiceAtLocations(managedServiceAtLocations);
    }

    if (service.getLanguages() != null) {
      List<Language> managedLanguages = service.getLanguages().stream()
          .map(language -> {
            if (language.getId()!= null) {
              return languageRepository.findById(language.getId())
                 .orElse(language);
            }
            return language;
          })
          .peek(language -> language.setService(service)).toList();
      service.setLanguages(managedLanguages);
    }

    if (service.getCostOptions() != null) {
      List<CostOption> managedCostOptions = service.getCostOptions().stream()
          .map(costOption -> {
            if (costOption.getId()!= null) {
              return costOptionRepository.findById(costOption.getId())
                 .orElse(costOption);
            }
            return costOption;
          })
          .peek(costOption -> costOption.setService(service)).toList();
      service.setCostOptions(managedCostOptions);
    }
    if (service.getRequiredDocuments() != null) {
      List<RequiredDocument> managedDocuments = service.getRequiredDocuments().stream()
          .map(requiredDocument -> {
            if (requiredDocument.getId()!= null) {
              return requiredDocumentRepository.findById(requiredDocument.getId())
                 .orElse(requiredDocument);
            }
            return requiredDocument;
          })
          .peek(requiredDocument -> requiredDocument.setService(service)).toList();
      service.setRequiredDocuments(managedDocuments);
    }

    if (service.getContacts() != null) {
      List<Contact> managedContacts = service.getContacts().stream()
          .map(contact -> {
            if (contact.getId()!= null) {
              return contactRepository.findById(contact.getId())
                 .orElse(contact);
            }
            return contact;
          })
          .peek(contact -> contact.setService(service)).toList();
      service.setContacts(managedContacts);
    }

    if (service.getFunding() != null) {
      List<Funding> managedFunding = service.getFunding().stream()
          .map(funding -> {
            if (funding.getId()!= null) {
              return fundingRepository.findById(funding.getId())
                 .orElse(funding);
            }
            return funding;
          })
          .peek(funding -> funding.setService(service)).toList();
      service.setFunding(managedFunding);
    }

    if (service.getAdditionalUrls() != null) {
      List<Url> managedUrls = service.getAdditionalUrls().stream()
          .map(url -> {
            if (url.getId()!= null) {
              return urlRepository.findById(url.getId())
                 .orElse(url);
            }
            return url;
          })
          .peek(url -> url.setService(service)).toList();
      service.setAdditionalUrls(managedUrls);
    }
  }

  @AfterMapping
  public Service toEntity(@MappingTarget Service service) {
    if (service.getOrganization().getId() != null) {
      service.setOrganization(organizationRepository.findById(service.getOrganization().getId()).orElseThrow(
          () -> new IllegalArgumentException("Organization not found for service with ID: " + service.getOrganization().getId())
      ));
    }

    if (service.getProgram().getId() != null) {
      service.setProgram(programRepository.findById(service.getProgram().getId()).orElseThrow(
          () -> new IllegalArgumentException("Program not found for service with ID: " + service.getProgram().getId())
      ));
    }
    return service;
  }

  public ServiceDTO.Response toResponseDTO(Service entity, MetadataService metadataService) {
    Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Service::getId,
        Response::setMetadata,
        SERVICE_RESOURCE_TYPE,
        metadataService
    );

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
    enrich(
        entity,
        summary,
        Service::getId,
        Summary::setMetadata,
        SERVICE_RESOURCE_TYPE,
        metadataService
    );

    enrichList(entity.getPhones(), summary::setPhones, (phone, meta) -> phoneMapper.toResponseDTO(phone, meta), metadataService);
    enrichList(entity.getSchedules(), summary::setSchedules, (schedule, meta) -> scheduleMapper.toResponseDTO(schedule, meta), metadataService);
    enrichList(entity.getServiceAreas(), summary::setServiceAreas, (area, meta) -> serviceAreaMapper.toResponseDTO(area, meta), metadataService);
    enrichList(entity.getLanguages(), summary::setLanguages, (lang, meta) -> languageMapper.toResponseDTO(lang, meta), metadataService);
    enrichList(entity.getCostOptions(), summary::setCostOptions, (cost, meta) -> costOptionMapper.toResponseDTO(cost, meta), metadataService);
    enrichList(entity.getRequiredDocuments(), summary::setRequiredDocuments, (doc, meta) -> requiredDocumentMapper.toResponseDTO(doc, meta), metadataService);
    enrichList(entity.getContacts(), summary::setContacts, (contact, meta) -> contactMapper.toResponseDTO(contact, meta), metadataService);
    enrichList(entity.getFunding(), summary::setFunding, (funding, meta) -> fundingMapper.toResponseDTO(funding, meta), metadataService);
    enrichList(entity.getAdditionalUrls(), summary::setAdditionalUrls, (url, meta) -> urlMapper.toResponseDTO(url, meta), metadataService);

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
}
