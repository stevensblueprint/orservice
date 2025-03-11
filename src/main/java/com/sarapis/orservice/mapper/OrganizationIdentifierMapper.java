package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.model.OrganizationIdentifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    uses = {AttributeMapper.class, MetadataMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrganizationIdentifierMapper {
  @Mapping(target = "organization", source = "organizationId", qualifiedByName = "stringToOrganization")
  OrganizationIdentifier toEntity(OrganizationIdentifierDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity);

  @Named("stringToOrganization")
  default Organization stringToOrganization(String organizationId) {
    if (organizationId == null) {
      return null;
    }
    Organization organization = new Organization();
    organization.setId(organizationId);
    return organization;
  }
}
