package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.model.Metadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataMapper {
  Metadata toEntity(MetadataDTO.Request dto);
  MetadataDTO.Response toDto(Metadata entity);
}
