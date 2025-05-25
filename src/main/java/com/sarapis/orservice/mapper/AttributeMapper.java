package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.model.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class, TaxonomyTermMapper.class})
public interface AttributeMapper {
  @Mapping(target = "taxonomyTerm.id", source = "taxonomyTerm.id")
  Attribute toEntity(AttributeDTO.Request dto);

  @Mapping(target = "taxonomyTerm.id", source = "taxonomyTerm.id")
  AttributeDTO.Response toResponseDTO(Attribute entity);
}
