package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.MetadataDTO.Response;
import com.sarapis.orservice.mapper.MetadataMapper;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.Metadata;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    List<com.sarapis.orservice.model.Metadata> metadataList = metadataRepository.findByResourceIdAndResourceType(resourceId, resourceType);
    return metadataList.stream().map(metadataMapper::toResponseDTO).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public <T> List<com.sarapis.orservice.model.Metadata> createMetadata(T original, T updated, String resourceType, Metadata actionType, String updatedBy) {
    // Validate inputs based on action type
    if (actionType == Metadata.UPDATE && (original == null || updated == null)) {
      throw new IllegalArgumentException("Both original and updated entities must be provided for UPDATE action");
    }
    if (actionType == Metadata.CREATE && updated == null) {
      throw new IllegalArgumentException("Updated entity must be provided for CREATE action");
    }
    if (actionType == Metadata.DELETE && original == null) {
      throw new IllegalArgumentException("Original entity must be provided for DELETE action");
    }

    List<com.sarapis.orservice.model.Metadata> metadataEntries = new ArrayList<>();

    // Handle based on action type
    switch (actionType) {
      case CREATE:
        metadataEntries = handleCreate(updated, resourceType, updatedBy);
        break;
      case UPDATE:
        metadataEntries = handleUpdate(original, updated, resourceType, updatedBy);
        break;
      case DELETE:
        metadataEntries = handleDelete(original, resourceType, updatedBy);
        break;
      default:
        throw new IllegalArgumentException("Unsupported action type: " + actionType);
    }

    return metadataEntries;
  }

  private <T> List<com.sarapis.orservice.model.Metadata> handleCreate(T created, String resourceType, String updatedBy) {
    List<com.sarapis.orservice.model.Metadata> metadataEntries = new ArrayList<>();
    String resourceId = getEntityId(created);

    if (resourceId == null) {
      throw new IllegalArgumentException("Entity must have an ID field");
    }

    try {
      Field[] fields = created.getClass().getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);

        // Skip collections and null fields
        if (field.getType().isAssignableFrom(List.class)) {
          continue;
        }

        Object value = field.get(created);

        // Only track non-null values for creation
        if (value != null) {
          MetadataDTO.Request metadata = MetadataDTO.Request.builder()
              .id(UUID.randomUUID().toString())
              .resourceId(resourceId)
              .resourceType(resourceType)
              .lastActionDate(LocalDate.now())
              .lastActionType(Metadata.CREATE.name())
              .fieldName(field.getName())
              .previousValue("null") // Previous value is null for creation
              .replacementValue(value.toString())
              .updatedBy(updatedBy)
              .build();

          com.sarapis.orservice.model.Metadata metadataEntity = metadataMapper.toEntity(metadata);
          metadataEntries.add(metadataEntity);
          metadataRepository.save(metadataEntity);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error accessing entity fields", e);
    }

    return metadataEntries;
  }

  private <T> List<com.sarapis.orservice.model.Metadata> handleUpdate(T original, T updated, String resourceType, String updatedBy) {
    List<com.sarapis.orservice.model.Metadata> metadataEntries = new ArrayList<>();
    String resourceId = getEntityId(updated);

    if (resourceId == null) {
      throw new IllegalArgumentException("Entity must have an ID field");
    }

    try {
      Field[] fields = original.getClass().getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);

        // Skip collections
        if (field.getType().isAssignableFrom(List.class)) {
          continue;
        }

        Object originalValue = field.get(original);
        Object updatedValue = field.get(updated);

        // Track changes between original and updated values
        if ((originalValue == null && updatedValue != null) ||
            (originalValue != null && !originalValue.equals(updatedValue))) {

          MetadataDTO.Request metadata = MetadataDTO.Request.builder()
              .id(UUID.randomUUID().toString())
              .resourceId(resourceId)
              .resourceType(resourceType)
              .lastActionDate(LocalDate.now())
              .lastActionType(Metadata.UPDATE.name())
              .fieldName(field.getName())
              .previousValue(originalValue != null ? originalValue.toString() : "null")
              .replacementValue(updatedValue != null ? updatedValue.toString() : "null")
              .updatedBy(updatedBy)
              .build();

          com.sarapis.orservice.model.Metadata metadataEntity = metadataMapper.toEntity(metadata);
          metadataEntries.add(metadataEntity);
          metadataRepository.save(metadataEntity);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error accessing entity fields", e);
    }

    return metadataEntries;
  }

  private <T> List<com.sarapis.orservice.model.Metadata> handleDelete(T deleted, String resourceType, String updatedBy) {
    List<com.sarapis.orservice.model.Metadata> metadataEntries = new ArrayList<>();
    String resourceId = getEntityId(deleted);

    if (resourceId == null) {
      throw new IllegalArgumentException("Entity must have an ID field");
    }

    try {
      Field[] fields = deleted.getClass().getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);

        // Skip collections
        if (field.getType().isAssignableFrom(List.class)) {
          continue;
        }

        Object value = field.get(deleted);

        // For deletions, track all non-null fields
        if (value != null) {
          MetadataDTO.Request metadata = MetadataDTO.Request.builder()
              .id(UUID.randomUUID().toString())
              .resourceId(resourceId)
              .resourceType(resourceType)
              .lastActionDate(LocalDate.now())
              .lastActionType(Metadata.DELETE.name())
              .fieldName(field.getName())
              .previousValue(value.toString())
              .replacementValue("null") // Replacement value is null for deletion
              .updatedBy(updatedBy)
              .build();

          com.sarapis.orservice.model.Metadata metadataEntity = metadataMapper.toEntity(metadata);
          metadataEntries.add(metadataEntity);
          metadataRepository.save(metadataEntity);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error accessing entity fields", e);
    }

    return metadataEntries;
  }

  private <T> String getEntityId(T entity) {
    try {
      Field idField = entity.getClass().getDeclaredField("id");
      idField.setAccessible(true);
      Object id = idField.get(entity);
      return id != null ? id.toString() : null;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      return null;
    }
  }
}