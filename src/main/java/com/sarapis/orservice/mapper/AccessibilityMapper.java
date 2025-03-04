package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.model.Accessibility;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface AccessibilityMapper {
  Accessibility toEntity(AccessibilityDTO.Request dto);

  AccessibilityDTO.Response toResponseDTO(Accessibility entity);
}
