package com.sarapis.orservice.mapper;

import static com.sarapis.orservice.utils.AttributeUtils.SCHEDULE_LINK_TYPE;
import static com.sarapis.orservice.utils.AttributeUtils.enrichAttributes;
import static com.sarapis.orservice.utils.AttributeUtils.saveAttributes;
import static com.sarapis.orservice.utils.MetadataUtils.SCHEDULE_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.model.Schedule;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.service.AttributeService;
import com.sarapis.orservice.service.MetadataService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ScheduleMapper {

  @Autowired
  private AttributeRepository attributeRepository;
  @Autowired
  private AttributeMapper attributeMapper;
  @Autowired
  private AttributeService attributeService;

  @Mapping(target = "service.id", source = "serviceId")
  @Mapping(target = "serviceAtLocation.id", source = "serviceAtLocationId")
  @Mapping(target = "location.id", source = "locationId")
  public abstract Schedule toEntity(ScheduleDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  @Mapping(target = "serviceAtLocationId", source = "serviceAtLocation.id")
  @Mapping(target = "locationId", source = "location.id")
  public abstract ScheduleDTO.Response toResponseDTO(Schedule entity);

  @AfterMapping
  protected void toEntity(ScheduleDTO.Request dto, @MappingTarget Schedule entity) {
    if (dto.getServiceId() == null) {
      entity.setService(null);
    }
    if (dto.getServiceAtLocationId() == null) {
      entity.setServiceAtLocation(null);
    }
    if (dto.getLocationId() == null) {
      entity.setLocation(null);
    }
    saveAttributes(
        attributeRepository,
        attributeMapper,
        dto.getAttributes(),
        SCHEDULE_LINK_TYPE
    );
  }

  protected ScheduleDTO.Response toResponseDTO(Schedule entity, MetadataService metadataService) {
    ScheduleDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        Schedule::getId,
        ScheduleDTO.Response::setMetadata,
        SCHEDULE_RESOURCE_TYPE,
        metadataService
    );
    enrichAttributes(
        entity,
        response,
        Schedule::getId,
        ScheduleDTO.Response::setAttributes,
        SCHEDULE_LINK_TYPE,
        attributeService
    );
    return response;
  }
}
