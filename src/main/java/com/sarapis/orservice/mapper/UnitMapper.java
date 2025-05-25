package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.UNIT_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.UNIT_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.UnitDTO;
import com.sarapis.orservice.model.Unit;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UnitMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  public abstract Unit toEntity(UnitDTO.Request dto);
  public abstract UnitDTO.Response toResponseDTO(Unit entity);

  @AfterMapping
  protected void toEntity(UnitDTO.Request dto, @MappingTarget Unit entity) {
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        UNIT_LINK_TYPE
    );
  }

  protected UnitDTO.Response toResponseDTO(Unit entity, MetadataService metadataService) {
    UnitDTO.Response dto = toResponseDTO(entity);
    enrich(
        entity,
        dto,
        Unit::getId,
        UnitDTO.Response::setMetadata,
        UNIT_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        dto,
        Unit::getId,
        UnitDTO.Response::setAttributes,
        UNIT_LINK_TYPE,
        attributeService
    );
    return dto;
  }
}
