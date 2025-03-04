package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.model.ServiceArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface ServiceAreaMapper {
  ServiceArea toEntity(ServiceAreaDTO.Request dto);

  ServiceAreaDTO.Response toResponseDTO(ServiceArea entity);
}
