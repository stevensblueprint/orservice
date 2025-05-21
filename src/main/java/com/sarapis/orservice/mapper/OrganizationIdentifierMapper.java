package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.model.OrganizationIdentifier;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

@Mapper(componentModel = "spring")
public interface OrganizationIdentifierMapper {

  @Mapping(target = "organization.id", source = "organizationId")
  OrganizationIdentifier toEntity(OrganizationIdentifierDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity);

  default OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity, MetadataService metadataService) {
    OrganizationIdentifierDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        OrganizationIdentifier::getId,
        OrganizationIdentifierDTO.Response::setMetadata,
        ORGANIZATION_RESOURCE_TYPE,
        metadataService
    );
    return response;
  }
}