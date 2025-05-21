package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AREA_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.model.ServiceArea;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class, MetadataMapper.class})
public interface ServiceAreaMapper {
  @Mapping(target = "service.id", source = "serviceId")
  ServiceArea toEntity(ServiceAreaDTO.Request dto);

  ServiceAreaDTO.Response toResponseDTO(ServiceArea entity);

  @AfterMapping
  default void toEntity(ServiceAreaDTO.Request dto, @MappingTarget ServiceArea entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceAtLocationId() == null) {
      entity.setServiceAtLocation(null);
    }
  }

  default ServiceAreaDTO.Response toResponseDTO(ServiceArea entity, MetadataService metadataService) {
    ServiceAreaDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        ServiceArea::getId,
        ServiceAreaDTO.Response::setMetadata,
        SERVICE_AREA_RESOURCE_TYPE,
        metadataService
    );
    return response;
  }
}
