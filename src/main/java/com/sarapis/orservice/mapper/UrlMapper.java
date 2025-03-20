package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UrlMapper {
  @Mapping(target = "organization.id", source = "organizationId")
  @Mapping(target = "service.id", source = "serviceId")
  Url toEntity(UrlDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  UrlDTO.Response toResponseDTO(Url entity);

  @AfterMapping
  default void toEntity(UrlDTO.Request dto, @MappingTarget() Url entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }
  }

  default UrlDTO.Response toResponseDTO(Url entity, MetadataService metadataService) {
    UrlDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(Url url, UrlDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            url.getId(), URL_RESOURCE_TYPE
        )
    );
  }
}
