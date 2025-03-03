package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class,
    ContactMapper.class, PhoneMapper.class, ScheduleMapper.class, ServiceAreaMapper.class, ServiceAreaMapper.class})
public interface ServiceMapper {
  Service toEntity(ServiceDTO.Request dto);

  @Mapping(target = "contacts", source = "contacts")
  @Mapping(target = "phones", source = "phones")
  @Mapping(target = "schedules", source = "schedules")
  @Mapping(target = "serviceAreas", source = "serviceAreas")
  ServiceDTO.Response toResponseDTO(Service entity);
}
