package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.model.OrganizationIdentifier;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sarapis.orservice.utils.AttributeUtils.ORGANIZATION_IDENTIFIER_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

@Mapper(componentModel = "spring")
public abstract class OrganizationIdentifierMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "organization.id", source = "organizationId")
  public abstract OrganizationIdentifier toEntity(OrganizationIdentifierDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  public abstract OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity);

  @AfterMapping
  protected void toEntity(OrganizationIdentifierDTO.Request dto, @MappingTarget OrganizationIdentifier entity) {
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        ORGANIZATION_IDENTIFIER_LINK_TYPE
    );
  }

  protected OrganizationIdentifierDTO.Response toResponseDTO(OrganizationIdentifier entity, MetadataService metadataService) {
    OrganizationIdentifierDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        OrganizationIdentifier::getId,
        OrganizationIdentifierDTO.Response::setMetadata,
        ORGANIZATION_IDENTIFIER_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        OrganizationIdentifier::getId,
        OrganizationIdentifierDTO.Response::setAttributes,
        ORGANIZATION_IDENTIFIER_LINK_TYPE,
        attributeService
    );
    return response;
  }
}