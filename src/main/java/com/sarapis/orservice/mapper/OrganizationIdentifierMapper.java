package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.model.OrganizationIdentifier;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE;

@Mapper(componentModel = "spring")
public interface OrganizationIdentifierMapper {

  @Mapping(target = "organization.id", source = "organizationId")
  OrganizationIdentifier toEntity(OrganizationIdentifierDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity);

  default OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity, MetadataService metadataService) {
    OrganizationIdentifierDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }
  default void enrichMetadata(OrganizationIdentifier identifier, OrganizationIdentifierDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            identifier.getId(), ORGANIZATION_IDENTIFIER_RESOURCE_TYPE
        )
    );
  }
}