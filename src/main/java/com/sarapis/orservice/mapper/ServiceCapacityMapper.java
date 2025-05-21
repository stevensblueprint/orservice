package com.sarapis.orservice.mapper;


import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_CAPACITY_RESOURCE_TYPE;

import com.sarapis.orservice.dto.ServiceCapacityDTO;
import com.sarapis.orservice.model.ServiceCapacity;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceCapacityMapper {
  @Mapping(target = "service.id", source = "serviceId")
  ServiceCapacity toEntity(ServiceCapacityDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  ServiceCapacityDTO.Response toResponseDTO(ServiceCapacity entity);

  @AfterMapping
  default void toEntity(ServiceCapacityDTO.Request dto, @MappingTarget ServiceCapacity entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
  }

  default ServiceCapacityDTO.Response toResponseDTO(ServiceCapacity entity, MetadataService metadataService) {
    ServiceCapacityDTO.Response response = toResponseDTO(entity);
    enrichMetadata(entity, response, metadataService);
    return response;
  }

  default void enrichMetadata(ServiceCapacity serviceCapacity, ServiceCapacityDTO.Response response, MetadataService metadataService) {
    response.setMetadata(
        metadataService.getMetadataByResourceIdAndResourceType(
            serviceCapacity.getId(), SERVICE_CAPACITY_RESOURCE_TYPE
        )
    );
  }
}
