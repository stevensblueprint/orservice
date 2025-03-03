package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface ScheduleMapper {
  Schedule toEntity(ScheduleDTO.Request dto);

  @Mapping(target = "attributes", source = "attributes")
  @Mapping(target = "metadata", source = "metadata")
  ScheduleDTO.Response toResponseDTO(Schedule entity);
}
