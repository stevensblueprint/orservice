package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.PHONE_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.PHONE_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.LanguageRepository;
import com.sarapis.orservice.service.AttributeService;
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

  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;


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
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        PHONE_LINK_TYPE
    );
  }

  public PhoneDTO.Response toResponseDTO(Phone entity,  MetadataService metadataService) {
    PhoneDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Phone::getId,
        PhoneDTO.Response::setMetadata,
        PHONE_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Phone::getId,
        PhoneDTO.Response::setAttributes,
        PHONE_LINK_TYPE,
        attributeService
    );

    if (entity.getLanguages()!= null) {
      List<LanguageDTO.Response> enrichedLanguages =
          entity.getLanguages().stream()
              .map(language -> languageMapper.toResponseDTO(language, metadataService)).toList();
      response.setLanguages(enrichedLanguages);
    }
    return response;
  }
}
