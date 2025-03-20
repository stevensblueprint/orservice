package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.ACCESSIBILITY_RESOURCE_TYPE;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.model.Accessibility;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface AccessibilityMapper {
  @Mapping(target = "location.id", source = "locationId")
  Accessibility toEntity(AccessibilityDTO.Request dto);

  @Mapping(target = "locationId", source = "location.id")
  AccessibilityDTO.Response toResponseDTO(Accessibility entity);

  @AfterMapping
  default void toEntity(AccessibilityDTO.Request dto, @MappingTarget() Accessibility entity) {
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
  }

  default AccessibilityDTO.Response toResponseDTO(Accessibility entity, MetadataService metadataService) {
    AccessibilityDTO.Response response = toResponseDTO(entity);
    enrichAccessibility(entity, response, metadataService);
    return response;
  }

  default void enrichAccessibility(Accessibility accessibility, AccessibilityDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            accessibility.getId(), ACCESSIBILITY_RESOURCE_TYPE
        )
    );
  }
}
