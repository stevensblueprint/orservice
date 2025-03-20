package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.LANGUAGE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.model.Language;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface LanguageMapper {
  @Mapping(target = "service.id", source = "serviceId")
  @Mapping(target = "location.id", source = "locationId")
  @Mapping(target = "phone.id", source = "phoneId")
  Language toEntity(LanguageDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  @Mapping(target = "locationId", source = "location.id")
  @Mapping(target = "phoneId", source = "phone.id")
  LanguageDTO.Response toResponseDTO(Language entity);

  @AfterMapping
  default void toEntity(LanguageDTO.Request dto, @MappingTarget() Language entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
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
