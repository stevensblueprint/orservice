package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.model.OrganizationIdentifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface OrganizationIdentifierMapper {
  OrganizationIdentifier toEntity(OrganizationIdentifierDTO.Request dto);
  OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity);
}
