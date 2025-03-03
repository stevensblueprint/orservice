package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.model.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface ProgramMapper {
  Program toEntity(ProgramDTO.Request dto);
  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  ProgramDTO.Response toResponseDTO(Program entity);
}
