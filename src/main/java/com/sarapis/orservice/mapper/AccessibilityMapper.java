package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.model.Accessibility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface AccessibilityMapper {
  Accessibility toEntity(AccessibilityDTO.Request dto);

  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  AccessibilityDTO.Response toResponseDTO(Accessibility entity);
}
