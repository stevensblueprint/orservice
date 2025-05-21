package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.MetadataUtils.SCHEDULE_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.model.Schedule;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

  @Mapping(target = "service.id", source = "serviceId")
  @Mapping(target = "serviceAtLocation.id", source = "serviceAtLocationId")
  @Mapping(target = "location.id", source = "locationId")
  Schedule toEntity(ScheduleDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  @Mapping(target = "serviceAtLocationId", source = "serviceAtLocation.id")
  @Mapping(target = "locationId", source = "location.id")
  ScheduleDTO.Response toResponseDTO(Schedule entity);

  @AfterMapping
  default void toEntity(ScheduleDTO.Request dto, @MappingTarget Schedule entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceAtLocationId() == null) {
      entity.setServiceAtLocation(null);
    }
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
  }

  default ScheduleDTO.Response toResponseDTO(Schedule entity, MetadataService metadataService) {
    ScheduleDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Schedule::getId,
        ScheduleDTO.Response::setMetadata,
        SCHEDULE_RESOURCE_TYPE,
        metadataService
    );
    return response;
  }
}
