package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.model.Organization;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {
        UrlMapper.class,
        FundingMapper.class,
        ContactMapper.class,
        PhoneMapper.class,
        ProgramMapper.class,
        OrganizationIdentifierMapper.class,
        LocationMapper.class}
)
public interface OrganizationMapper {
  @Mapping(target = "id", ignore = true)
  Organization toEntity(OrganizationDTO.Request dto);
  OrganizationDTO.Response toResponseDTO(Organization entity);

  @AfterMapping
  default void setOrganizationIdentifiers(@MappingTarget Organization organization) {
    if (organization.getOrganizationIdentifiers() != null) {
      organization.getOrganizationIdentifiers().forEach(identifier -> {
        identifier.setOrganization(organization);
      });
    }

    if (organization.getPrograms() != null) {
      organization.getPrograms().forEach(program -> {
        program.setOrganization(organization);
      });
    }
  }
}
