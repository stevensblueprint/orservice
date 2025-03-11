package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ProgramDTO;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.model.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    uses = {AttributeMapper.class, MetadataMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProgramMapper {
  @Mapping(target = "organization", source = "organizationId", qualifiedByName = "stringToOrganization")
  Program toEntity(ProgramDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  ProgramDTO.Response toResponseDTO(Program entity);

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
