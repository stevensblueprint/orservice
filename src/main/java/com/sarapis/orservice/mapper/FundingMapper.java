package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.FUNDING_RESOURCE_TYPE;

import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.model.Funding;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FundingMapper {
  @Mapping(target = "organization.id", source = "organizationId")
  @Mapping(target = "service.id", source = "serviceId")
  Funding toEntity(FundingDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  FundingDTO.Response toResponseDTO(Funding entity);

  @AfterMapping
  default void toEntity(FundingDTO.Response dto, @MappingTarget() Funding entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }

    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }
  }

  default FundingDTO.Response toResponseDTO(Funding entity, MetadataService metadataService) {
    FundingDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(Funding funding, FundingDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            funding.getId(), FUNDING_RESOURCE_TYPE
        )
    );
  }

}
