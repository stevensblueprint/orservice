package com.sarapis.orservice.utils;

import static com.sarapis.orservice.utils.MetadataType.CREATE;
import static com.sarapis.orservice.utils.MetadataType.DELETE;
import static com.sarapis.orservice.utils.MetadataType.UPDATE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sarapis.orservice.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
  public static final String EMPTY_PREVIOUS_VALUE = "";

  public static final Map<MetadataType, String> ACTION_COMPLEMENT_MAP = Map.of(
          CREATE, DELETE.toString(),
          DELETE, CREATE.toString(),
          UPDATE, UPDATE.toString()
  );

  public static final Map<String, MetadataType> ACTION_MAP = Map.of(
          CREATE.toString(), CREATE,
          DELETE.toString(), DELETE,
          UPDATE.toString(), UPDATE
  );

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

  public static <T> void undoMetadata(Metadata metadata,
                                      JpaRepository<T, String> repository,
                                      Map<String, BiConsumer<T, String>> fieldMap) {
    String resId = metadata.getResourceId();
    T entity = repository.findById(resId)
            .orElseThrow();

    String fieldName = metadata.getFieldName();
    BiConsumer<T, String> setter = fieldMap.get(fieldName);

    String prevValue = metadata.getPreviousValue();
    setter.accept(entity, prevValue);

    // TODO: Save metadata to entity
    Metadata newMeta = new Metadata();
    repository.save(entity);
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

  public static String getComplementAction(String actionType) {
      MetadataType action = ACTION_MAP.get(actionType);
      return ACTION_COMPLEMENT_MAP.get(action);
  }
}
