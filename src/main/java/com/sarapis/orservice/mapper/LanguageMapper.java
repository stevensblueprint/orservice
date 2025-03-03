package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.model.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface LanguageMapper {
  Language toEntity(LanguageDTO.Request dto);

  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  LanguageDTO.Response toResponseDTO(Language entity);
}
