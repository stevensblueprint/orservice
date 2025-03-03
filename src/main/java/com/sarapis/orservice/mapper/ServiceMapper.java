package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.model.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
  Service toEntity(ServiceDTO.Request dto);

  ServiceDTO.Response toResponseDTO(Service entity);
}
