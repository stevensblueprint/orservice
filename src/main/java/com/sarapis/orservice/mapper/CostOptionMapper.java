package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.CostOptionDTO;
import com.sarapis.orservice.model.CostOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface CostOptionMapper {
  CostOption toEntity(CostOptionDTO.Request dto);

  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  CostOptionDTO.Response toResponseDTO(CostOption entity);
}
