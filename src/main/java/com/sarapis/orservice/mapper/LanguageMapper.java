package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.LANGUAGE_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.LANGUAGE_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LanguageMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  public abstract Language toEntity(LanguageDTO.Request dto);
  public abstract LanguageDTO.Response toResponseDTO(Language entity);

  @AfterMapping
  protected void toEntity(LanguageDTO.Request dto, @MappingTarget() Language entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID().toString());
    }
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
    if (dto.getPhoneId() == null) {
      entity.setPhone(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        LANGUAGE_LINK_TYPE
    );
  }

  protected LanguageDTO.Response toResponseDTO(Language entity,  MetadataService metadataService) {
    LanguageDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Language::getId,
        LanguageDTO.Response::setMetadata,
        LANGUAGE_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Language::getId,
        LanguageDTO.Response::setAttributes,
        LANGUAGE_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
