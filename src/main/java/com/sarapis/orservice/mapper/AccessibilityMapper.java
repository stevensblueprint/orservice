package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.ACCESSIBILITY_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.ACCESSIBILITY_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.model.Accessibility;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class AccessibilityMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "location.id", source = "locationId")
  public abstract Accessibility toEntity(AccessibilityDTO.Request dto);

  @Mapping(target = "locationId", source = "location.id")
  public abstract AccessibilityDTO.Response toResponseDTO(Accessibility entity);

  @AfterMapping
  protected void toEntity(AccessibilityDTO.Request dto, @MappingTarget Accessibility entity) {
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        ACCESSIBILITY_LINK_TYPE
    );
  }

  protected AccessibilityDTO.Response toResponseDTO(Accessibility entity, MetadataService metadataService) {
    AccessibilityDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Accessibility::getId,
        AccessibilityDTO.Response::setMetadata,
        ACCESSIBILITY_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Accessibility::getId,
        AccessibilityDTO.Response::setAttributes,
        ACCESSIBILITY_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
