package com.sarapis.orservice.mapper;


import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface LocationMapper {
  Location toEntity(LocationDTO.Request dto);

  LocationDTO.Response toResponseDTO(Location entity);
}
