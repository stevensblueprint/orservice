package com.sarapis.orservice.utils;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.service.MetadataService;
import java.time.LocalDate;
import java.util.UUID;

public class MetadataUtils {
  public static final String ORGANIZATION_RESOURCE_TYPE = "ORGANIZATION";
  public static final String URL_RESOURCE_TYPE = "URL";
  public static final String PROGRAM_RESOURCE_TYPE = "PROGRAM";
  public static final String PHONE_RESOURCE_TYPE = "PHONE";
  public static final String ORGANIZATION_IDENTIFIER_RESOURCE_TYPE = "ORGANIZATION_IDENTIFIER";
  public static final String FUNDING_RESOURCE_TYPE = "FUNDING_RESOURCE";
  public static final String CONTACT_RESOURCE_TYPE = "CONTACT";
  public static final String LANGUAGE_RESOURCE_TYPE = "LANGUAGE";
  public static final String EMPTY_PREVIOUS_VALUE = "";
  public static void createMetadataEntry(
      MetadataService metadataService,
      String resourceId,
      String resourceType,
      String actionType,
      String fieldName,
      String previousValue,
      String replacementValue,
      String updatedBy) {

    MetadataDTO.Request metadataRequest = new MetadataDTO.Request();
    metadataRequest.setId(UUID.randomUUID().toString());
    metadataRequest.setResourceId(resourceId);
    metadataRequest.setResourceType(resourceType);
    metadataRequest.setLastActionDate(LocalDate.now());
    metadataRequest.setLastActionType(actionType);
    metadataRequest.setFieldName(fieldName);
    metadataRequest.setPreviousValue(previousValue);
    metadataRequest.setReplacementValue(replacementValue);
    metadataRequest.setUpdatedBy(updatedBy);

    metadataService.createMetadata(metadataRequest);
  }
}
