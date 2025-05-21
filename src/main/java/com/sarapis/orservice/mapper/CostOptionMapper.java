package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.COST_OPTION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.model.CostOption;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface CostOptionMapper {
  @Mapping(target = "service.id", source = "serviceId")
  CostOption toEntity(CostOptionDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  CostOptionDTO.Response toResponseDTO(CostOption entity);

  @AfterMapping
  default void toEntity(CostOptionDTO.Response dto, @MappingTarget() CostOption entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
  }

  default CostOptionDTO.Response toResponseDTO(CostOption entity, MetadataService metadataService) {
    CostOptionDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        CostOption::getId,
        CostOptionDTO.Response::setMetadata,
        COST_OPTION_RESOURCE_TYPE,
        metadataService
    );
    return response;
  }
}
