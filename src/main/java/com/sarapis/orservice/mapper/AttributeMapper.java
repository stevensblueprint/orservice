package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.model.Attribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class, TaxonomyTermMapper.class})
public interface AttributeMapper {
  Attribute toEntity(AttributeDTO.Request dto);

  AttributeDTO.Response toResponseDTO(Attribute entity);
}
