package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.COST_OPTION_RESOURCE_TYPE;

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
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(CostOption costOption, CostOptionDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            costOption.getId(), COST_OPTION_RESOURCE_TYPE
        )
    );
  }

}
