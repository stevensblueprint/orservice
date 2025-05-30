package com.sarapis.orservice.utils;

import static com.sarapis.orservice.utils.MetadataType.CREATE;
import static com.sarapis.orservice.utils.MetadataType.UPDATE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.service.MetadataService;
import java.util.function.Function;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class MetadataUtils {
  public static final String ORGANIZATION_RESOURCE_TYPE = "ORGANIZATION";
  public static final String URL_RESOURCE_TYPE = "URL";
  public static final String PROGRAM_RESOURCE_TYPE = "PROGRAM";
  public static final String PHONE_RESOURCE_TYPE = "PHONE";
  public static final String ORGANIZATION_IDENTIFIER_RESOURCE_TYPE = "ORGANIZATION_IDENTIFIER";
  public static final String FUNDING_RESOURCE_TYPE = "FUNDING_RESOURCE";
  public static final String CONTACT_RESOURCE_TYPE = "CONTACT";
  public static final String LANGUAGE_RESOURCE_TYPE = "LANGUAGE";
  public static final String ACCESSIBILITY_RESOURCE_TYPE = "ACCESSIBILITY";
  public static final String ADDRESS_RESOURCE_TYPE = "ADDRESS";
  public static final String SCHEDULE_RESOURCE_TYPE = "SCHEDULE";
  public static final String LOCATION_RESOURCE_TYPE = "LOCATION";
  public static final String SERVICE_AREA_RESOURCE_TYPE = "SERVICE_AREA";
  public static final String COST_OPTION_RESOURCE_TYPE = "COST_OPTION";
  public static final String SERVICE_RESOURCE_TYPE = "SERVICE";
  public static final String REQUIRED_DOCUMENT_RESOURCE_TYPE = "REQUIRED_DOCUMENT";
  public static final String SERVICE_AT_LOCATION_RESOURCE_TYPE = "SERVICE_AT_LOCATION";
  public static final String TAXONOMY_TERM_RESOURCE_TYPE = "TAXONOMY_TERM";
  public static final String TAXONOMY_RESOURCE_TYPE = "TAXONOMY";
  public static final String UNIT_RESOURCE_TYPE = "UNIT";
  public static final String SERVICE_CAPACITY_RESOURCE_TYPE = "SERVICE_CAPACITY";
  public static final String EMPTY_PREVIOUS_VALUE = "";

  public static <T> List<Metadata> createMetadata(T original, T updated, String resourceId, String resourceType, MetadataType actionType, String updatedBy) {
    if (actionType == MetadataType.UPDATE && (original == null || updated == null)) {
      throw new IllegalArgumentException("Both original and updated entities must be provided for UPDATE action");
    }
    if (actionType == MetadataType.CREATE && updated == null) {
      throw new IllegalArgumentException("Updated entity must be provided for CREATE action");
    }
    if (actionType == MetadataType.DELETE && original == null) {
      throw new IllegalArgumentException("Original entity must be provided for DELETE action");
    }
    return switch (actionType) {
      case CREATE -> handleCreate(updated, resourceId, resourceType, updatedBy);
      default -> throw new IllegalArgumentException("");
    };
  }

  public static <T> T undoMetadata(Metadata metadata,
                                   MetadataRepository metadataRepository,
                                   JpaRepository<T, String> repository,
                                   Map<String, BiConsumer<T, String>> fieldMap,
                                   LocalDate actionDate,
                                   String updatedBy) {
    String resId = metadata.getResourceId();
    T entity = repository.findById(resId)
            .orElseThrow();

    String fieldName = metadata.getFieldName();
    BiConsumer<T, String> setter = fieldMap.get(fieldName);

    String prevValue = metadata.getPreviousValue();
    setter.accept(entity, prevValue);

    Metadata newMeta = new Metadata();
    newMeta.setId(UUID.randomUUID().toString());
    newMeta.setResourceId(resId);
    newMeta.setResourceType(metadata.getResourceType());
    newMeta.setLastActionDate(actionDate);
    newMeta.setLastActionType(UPDATE.toString());
    newMeta.setFieldName(fieldName);
    newMeta.setPreviousValue(metadata.getReplacementValue());
    newMeta.setReplacementValue(metadata.getPreviousValue());
    newMeta.setUpdatedBy(updatedBy);

    repository.save(entity);
    metadataRepository.save(newMeta);

    return entity;
  }

  public static <T> T undoMetadata(Metadata metadata,
                                   MetadataRepository metadataRepository,
                                   JpaRepository<T, String> repository,
                                   Map<String, BiConsumer<T, String>> fieldMap,
                                   String updatedBy) {
      return undoMetadata(
              metadata,
              metadataRepository,
              repository,
              fieldMap,
              LocalDate.now(),
              updatedBy
      );
  }

  public static <T> T undoMetadataBatch(List<Metadata> metadataList,
                                        MetadataRepository metadataRepository,
                                        JpaRepository<T, String> repository,
                                        Map<String, BiConsumer<T, String>> fieldMap,
                                        String updatedBy) {
    LocalDate actionDate = LocalDate.now();
    T latest = null;    // Organization after all undo ops

    for(Metadata metadata : metadataList) {
      latest = undoMetadata(
              metadata,
              metadataRepository,
              repository,
              fieldMap,
              actionDate,
              updatedBy
      );
    }
    return latest;
  }

  public static <T> List<Metadata> handleCreate(T created, String resourceId, String resourceType, String updatedBy) {
    List<Metadata> metadataEntries = new ArrayList<>();
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
          Metadata metadata = new Metadata();
          metadata.setId(UUID.randomUUID().toString());
          metadata.setResourceId(resourceId);
          metadata.setResourceType(resourceType);
          metadata.setLastActionDate(LocalDate.now());
          metadata.setLastActionType(CREATE.name());
          metadata.setFieldName(field.getName());
          metadata.setPreviousValue(EMPTY_PREVIOUS_VALUE);
          metadata.setReplacementValue(value.toString());
          metadata.setUpdatedBy(updatedBy);
          metadataEntries.add(metadata);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Error accessing entity fields", e);
    }
    return metadataEntries;
  }

  public static <T, R> void enrich(
      T resource,
      R response,
      Function<T, String> idExtractor,
      BiConsumer<R, List<MetadataDTO.Response>> metadataSetter,
      String resourceType,
      MetadataService metadataService) {
    List<MetadataDTO.Response> md = metadataService
        .getMetadataByResourceIdAndResourceType(
            idExtractor.apply(resource),
            resourceType
        );
    metadataSetter.accept(response, md);
  }
}
