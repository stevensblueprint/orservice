package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.PHONE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class PhoneMapper {
  @Autowired
  private LanguageMapper languageMapper;
  @Autowired
  private LanguageRepository languageRepository;


  @Mapping(target = "organization.id", source = "organizationId")
  @Mapping(target = "service.id", source = "serviceId")
  @Mapping(target = "serviceAtLocation.id", source = "serviceAtLocationId")
  @Mapping(target = "location.id", source = "locationId")
  @Mapping(target = "contact.id", source = "contactId")
  public abstract Phone toEntity(PhoneDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  @Mapping(target = "serviceAtLocationId", source = "serviceAtLocation.id")
  @Mapping(target = "locationId", source = "location.id")
  @Mapping(target = "contactId", source = "contact.id")
  public abstract  PhoneDTO.Response toResponseDTO(Phone entity);

  @AfterMapping
  protected void toEntity(PhoneDTO.Request dto, @MappingTarget() Phone entity) {
    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceAtLocationId() == null) {
      entity.setServiceAtLocation(null);
    }
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
    if (dto.getContactId() == null) {
      entity.setContact(null);
    }
    if (entity.getLanguages() != null) {
      List<Language> managedLanguages = entity.getLanguages().stream()
         .map(language -> {
            if (language.getId() != null) {
              return languageRepository.findById(language.getId())
                 .orElse(language);
            }
            return language;
          })
         .peek(language -> language.setPhone(entity)).toList();
      entity.setLanguages(managedLanguages);
    }
  }

  public PhoneDTO.Response toResponseDTO(Phone entity,  MetadataService metadataService) {
    PhoneDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    if (entity.getLanguages()!= null) {
      List<LanguageDTO.Response> enrichedLanguages =
          entity.getLanguages().stream()
              .map(language -> languageMapper.toResponseDTO(language, metadataService)).toList();
      response.setLanguages(enrichedLanguages);
    }
    return response;
  }

  protected void enrichMetadata(Phone phone, PhoneDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            phone.getId(), PHONE_RESOURCE_TYPE
        )
    );
  }
}
