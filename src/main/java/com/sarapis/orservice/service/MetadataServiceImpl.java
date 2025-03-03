package com.sarapis.orservice.service;

import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.MetadataRepository;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {
  private final MetadataRepository metadataRepository;

  @Override
  @Transactional
  public void createInitialMetadata(String resourceId, String resourceType, String fieldName, String value, String updatedBy) {
    Metadata metadata = new Metadata();
    metadata.setId(UUID.randomUUID().toString());
    metadata.setResourceId(resourceId);
    metadata.setResourceType(resourceType);
    metadata.setLastActionDate(LocalDate.now());
    metadata.setLastActionType("create");
    metadata.setFieldName(fieldName);
    metadata.setPreviousValue("");
    metadata.setReplacementValue(value);
    metadata.setUpdatedBy(updatedBy);
    metadataRepository.save(metadata);
  }

  @Override
  @Transactional
  public void createMetadata(String resourceId, String resourceType, String fieldName, String previousValue,
      String replacementValue, String updatedBy) {
    Metadata metadata = new Metadata();
    metadata.setId(UUID.randomUUID().toString());
    metadata.setResourceId(resourceId);
    metadata.setResourceType(resourceType);
    metadata.setLastActionDate(LocalDate.now());
    metadata.setLastActionType("update");
    metadata.setFieldName(fieldName);
    metadata.setPreviousValue(previousValue);
    metadata.setReplacementValue(replacementValue);
    metadata.setUpdatedBy(updatedBy);
    metadataRepository.save(metadata);
  }
}
