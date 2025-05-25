package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.URL_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UrlMapper {
  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "organization.id", source = "organizationId")
  @Mapping(target = "service.id", source = "serviceId")
  protected abstract Url toEntity(UrlDTO.Request dto);

  @Mapping(target = "organizationId", source = "organization.id")
  @Mapping(target = "serviceId", source = "service.id")
  protected abstract UrlDTO.Response toResponseDTO(Url entity);

  @AfterMapping
  protected void toEntity(UrlDTO.Request dto, @MappingTarget() Url entity) {
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
        URL_LINK_TYPE
    );
  }

  protected UrlDTO.Response toResponseDTO(Url entity, MetadataService metadataService) {
    UrlDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Url::getId,
        UrlDTO.Response::setMetadata,
        URL_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Url::getId,
        UrlDTO.Response::setAttributes,
        URL_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
