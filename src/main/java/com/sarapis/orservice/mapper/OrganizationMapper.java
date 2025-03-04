package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.model.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
  Organization toEntity(OrganizationDTO.Request dto);
  OrganizationDTO.Response toResponseDTO(Organization entity);
}
