package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.ACCESSIBILITY_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.dto.AccessibilityDTO.Request;
import com.sarapis.orservice.dto.AccessibilityDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.mapper.AccessibilityMapper;
import com.sarapis.orservice.model.Accessibility;
import com.sarapis.orservice.repository.AccessibilityRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccessibilityServiceImpl implements AccessibilityService {

  private final AccessibilityRepository accessibilityRepository;
  private final AccessibilityMapper accessibilityMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createAccessibility(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Accessibility accessibility = accessibilityMapper.toEntity(dto);
    Accessibility savedAccessibility = accessibilityRepository.save(accessibility);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedAccessibility.getId(),
        ACCESSIBILITY_RESOURCE_TYPE,
        CREATE.name(),
        "accessibility",
        EMPTY_PREVIOUS_VALUE,
        accessibilityMapper.toResponseDTO(savedAccessibility).toString(),
        "SYSTEM"
    );
    AccessibilityDTO.Response response = accessibilityMapper.toResponseDTO(savedAccessibility);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedAccessibility.getId(), ACCESSIBILITY_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getAccessibilityByLocationId(String locationId) {
    List<Accessibility> accessibilityList = accessibilityRepository.findByLocationId(locationId);
    List<AccessibilityDTO.Response> accessibilityDtos = accessibilityList.stream().map(accessibilityMapper::toResponseDTO).toList();
    accessibilityDtos = accessibilityDtos.stream().peek(accessibility -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          accessibility.getId(), ACCESSIBILITY_RESOURCE_TYPE
      );
      accessibility.setMetadata(metadata);
    }).toList();
    return accessibilityDtos;
  }
}
