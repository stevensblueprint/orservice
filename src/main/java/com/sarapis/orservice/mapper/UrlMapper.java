package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Url;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface UrlMapper {
  Url toEntity(UrlDTO.Request dto);

  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  UrlDTO.Response toResponseDTO(Url entity);

  void updateEntityFromDto(UrlDTO.UpdateRequest dto, @MappingTarget Url entity);
}
