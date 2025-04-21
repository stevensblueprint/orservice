package com.sarapis.orservice.service;

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

  @Override
  @Transactional(readOnly = true)
  public List<Response> getMetadataByResourceIdAndResourceType(String resourceId, String resourceType) {
    List<Metadata> metadataList = metadataRepository.findByResourceIdAndResourceType(resourceId, resourceType);
    return metadataList.stream().map(metadataMapper::toResponseDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<Response> getMetadataByFileImportIdAndResourceType(String fileImportId, String resourceType) {
    List<Metadata> metadataList = metadataRepository.findByFileImportIdAndResourceType(fileImportId, resourceType);
    return metadataList.stream().map(metadataMapper::toResponseDTO).collect(Collectors.toList());
  }
}
