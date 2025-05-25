package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.SERVICE_AREA_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AREA_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.model.ServiceArea;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public abstract class ServiceAreaMapper {

  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "service.id", source = "serviceId")
  public abstract ServiceArea toEntity(ServiceAreaDTO.Request dto);

  public abstract ServiceAreaDTO.Response toResponseDTO(ServiceArea entity);

  @AfterMapping
  protected void toEntity(ServiceAreaDTO.Request dto, @MappingTarget ServiceArea entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceAtLocationId() == null) {
      entity.setServiceAtLocation(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        SERVICE_AREA_LINK_TYPE
    );
  }

  protected ServiceAreaDTO.Response toResponseDTO(ServiceArea entity, MetadataService metadataService) {
    ServiceAreaDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        ServiceArea::getId,
        ServiceAreaDTO.Response::setMetadata,
        SERVICE_AREA_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        ServiceArea::getId,
        ServiceAreaDTO.Response::setAttributes,
        SERVICE_AREA_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
