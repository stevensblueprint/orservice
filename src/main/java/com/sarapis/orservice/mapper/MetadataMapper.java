package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.model.Metadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataMapper {
  Metadata toEntity(MetadataDTO.Request dto);
  Metadata toEntity(MetadataDTO.Response dto);
  MetadataDTO.Response toResponseDTO(Metadata entity);
}
