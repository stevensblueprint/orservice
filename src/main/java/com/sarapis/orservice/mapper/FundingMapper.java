package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.FUNDING_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.FUNDING_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.FundingDTO;
import com.sarapis.orservice.model.Funding;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FundingMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "organization.id", source = "organizationId")
  @Mapping(target = "service.id", source = "serviceId")
  public abstract Funding toEntity(FundingDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  public abstract FundingDTO.Response toResponseDTO(Funding entity);

  @AfterMapping
  protected void toEntity(FundingDTO.Request dto, @MappingTarget() Funding entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }

    if (dto.getOrganizationId() == null) {
      entity.setOrganization(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        FUNDING_LINK_TYPE
    );
  }

  protected FundingDTO.Response toResponseDTO(Funding entity, MetadataService metadataService) {
    FundingDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Funding::getId,
        FundingDTO.Response::setMetadata,
        FUNDING_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Funding::getId,
        FundingDTO.Response::setAttributes,
        FUNDING_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
