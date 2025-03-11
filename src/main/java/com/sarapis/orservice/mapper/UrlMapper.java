package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.model.Url;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    uses = {AttributeMapper.class, MetadataMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UrlMapper {
  @Mapping(target = "organization", source = "organizationId", qualifiedByName = "stringToOrganization")
  @Mapping(target = "service", source = "serviceId", qualifiedByName = "stringToService")
  Url toEntity(UrlDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  UrlDTO.Response toResponseDTO(Url entity);

  @Named("stringToOrganization")
  default Organization stringToOrganization(String organizationId) {
    if (organizationId == null) {
      return null;
    }
    Organization organization = new Organization();
    organization.setId(organizationId);
    return organization;
  }

  @Named("stringToService")
  default Service stringToService(String serviceId) {
    if (serviceId == null) {
      return null;
    }
    Service service = new Service();
    service.setId(serviceId);
    return service;
  }
}
