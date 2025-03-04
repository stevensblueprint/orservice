package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.model.ServiceAtLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class, ContactMapper.class, PhoneMapper.class, ScheduleMapper.class})
public interface ServiceAtLocationMapper {
  ServiceAtLocation toEntity(ServiceAtLocationDTO.Request dto);

  @Mapping(target = "contacts", source = "contacts")
  @Mapping(target = "phones", source = "phones")
  @Mapping(target = "schedules", source = "schedules")
  ServiceAtLocationDTO.Response toResponseDTO(ServiceAtLocation entity);
}
