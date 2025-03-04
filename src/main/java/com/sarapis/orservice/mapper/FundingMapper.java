package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.model.Funding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface FundingMapper {
  Funding toEntity(FundingDTO.Request dto);

  FundingDTO.Response toResponseDTO(Funding entity);
}
