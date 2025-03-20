package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.LANGUAGE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.service.MetadataService;
import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
  Language toEntity(LanguageDTO.Request dto);
  LanguageDTO.Response toResponseDTO(Language entity);

  @AfterMapping
  default void toEntity(LanguageDTO.Request dto, @MappingTarget() Language entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID().toString());
    }
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
    if (dto.getPhoneId() == null) {
      entity.setPhone(null);
    }
  }

  default LanguageDTO.Response toResponseDTO(Language entity,  MetadataService metadataService) {
    LanguageDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(Language language, LanguageDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            language.getId(), LANGUAGE_RESOURCE_TYPE
        )
    );
  }
}
