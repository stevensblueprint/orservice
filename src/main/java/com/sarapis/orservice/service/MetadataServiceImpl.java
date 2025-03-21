package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;
import com.sarapis.orservice.dto.MetadataDTO.Response;
import com.sarapis.orservice.mapper.MetadataMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.MetadataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {
  private final MetadataRepository metadataRepository;
  private final MetadataMapper metadataMapper;

  private final OrganizationService organizationService;

  @Override
  @Transactional(readOnly = true)
  public List<Response> getMetadataByResourceIdAndResourceType(String resourceId, String resourceType) {
    List<Metadata> metadataList = metadataRepository.findByResourceIdAndResourceType(resourceId, resourceType);
    return metadataList.stream().map(metadataMapper::toResponseDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void undoMetadata(String metadataId) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow();

    // TODO: Add remaining services
    String resourceType = metadata.getResourceType();
    switch(resourceType) {
      case ORGANIZATION_RESOURCE_TYPE -> this.organizationService.undoOrganizationMetadata(metadata);
      default -> throw new RuntimeException("");
    }
  }
}
