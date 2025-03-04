package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.SERVICE_AREA_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO.Request;
import com.sarapis.orservice.dto.ServiceAreaDTO.Response;
import com.sarapis.orservice.mapper.ServiceAreaMapper;
import com.sarapis.orservice.model.ServiceArea;
import com.sarapis.orservice.repository.ServiceAreaRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceAreaServiceImpl implements ServiceAreaService{
  private final ServiceAreaRepository serviceAreaRepository;
  private final ServiceAreaMapper serviceAreaMapper;
  private final MetadataService metadataService;
  @Override
  public Response createServiceArea(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    ServiceArea area = serviceAreaMapper.toEntity(dto);
    ServiceArea savedArea = serviceAreaRepository.save(area);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedArea.getId(),
        SERVICE_AREA_RESOURCE_TYPE,
        CREATE.name(),
        "service_area",
        EMPTY_PREVIOUS_VALUE,
        dto.getName(),
        "SYSTEM"
    );
    ServiceAreaDTO.Response response = serviceAreaMapper.toResponseDTO(savedArea);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedArea.getId(), SERVICE_AREA_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  public List<Response> getServiceAreasByServiceId(String serviceId) {
    List<ServiceArea> areas = serviceAreaRepository.findServiceAreaByServiceId(serviceId);
    List<ServiceAreaDTO.Response> response = areas.stream().map(serviceAreaMapper::toResponseDTO).toList();
    response = response.stream().peek(area -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          area.getId(), SERVICE_AREA_RESOURCE_TYPE
      );
      area.setMetadata(metadata);
    }).toList();
    return response;
  }
}
