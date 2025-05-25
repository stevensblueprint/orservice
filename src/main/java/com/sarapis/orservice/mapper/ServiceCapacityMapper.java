package com.sarapis.orservice.mapper;


import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_CAPACITY_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.enrich;

import com.sarapis.orservice.dto.ServiceCapacityDTO;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.model.ServiceCapacity;
import com.sarapis.orservice.model.Unit;
import com.sarapis.orservice.repository.ServiceRepository;
import com.sarapis.orservice.repository.UnitRepository;
import com.sarapis.orservice.service.MetadataService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ServiceCapacityMapper {
  @Autowired
  private ServiceRepository serviceRepository;
  @Autowired
  private UnitRepository unitRepository;

  @Mapping(target = "service.id", source = "serviceId")
  public abstract ServiceCapacity toEntity(ServiceCapacityDTO.Request dto);

  @Mapping(target = "serviceId", source = "service.id")
  public abstract ServiceCapacityDTO.Response toResponseDTO(ServiceCapacity entity);

  @AfterMapping
  public ServiceCapacity toEntity(@MappingTarget ServiceCapacity entity) {
    Optional.ofNullable(entity.getService())
        .map(Service::getId)
        .ifPresent(serviceId -> {
          Service service = serviceRepository.findById(serviceId)
              .orElseThrow(() -> new EntityNotFoundException("Service not found with ID: " + serviceId));
          entity.setService(service);
        });
    Optional.ofNullable(entity.getUnit())
        .map(Unit::getId)
        .ifPresent(unitId -> {
          Unit unit = unitRepository.findById(unitId)
              .orElseThrow(() -> new EntityNotFoundException("Unit not found with ID: " + unitId));
          entity.setUnit(unit);
        });
    return entity;
  }

  public ServiceCapacityDTO.Response toResponseDTO(ServiceCapacity entity, MetadataService metadataService) {
    ServiceCapacityDTO.Response response = toResponseDTO(entity);
    enrich(
        entity,
        response,
        ServiceCapacity::getId,
        ServiceCapacityDTO.Response::setMetadata,
        SERVICE_CAPACITY_RESOURCE_TYPE,
        metadataService
    );
    return response;
  }
}
