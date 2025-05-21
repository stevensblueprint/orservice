package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.UNIT_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.UnitDTO;
import com.sarapis.orservice.model.Unit;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {
  Unit toEntity(UnitDTO.Request dto);
  UnitDTO.Response toResponseDTO(Unit entity);

  default UnitDTO.Response toResponseDTO(Unit entity, MetadataService metadataService) {
    UnitDTO.Response dto = toResponseDTO(entity);
    enrich(
        entity,
        dto,
        Unit::getId,
        UnitDTO.Response::setMetadata,
        UNIT_RESOURCE_TYPE,
        metadataService
    );
    return dto;
  }
}
