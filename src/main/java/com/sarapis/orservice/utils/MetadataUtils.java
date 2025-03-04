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
  public static final String ACCESSIBILITY_RESOURCE_TYPE = "ACCESSIBILITY";
  public static final String ADDRESS_RESOURCE_TYPE = "ADDRESS";
  public static final String SCHEDULE_RESOURCE_TYPE = "SCHEDULE";
  public static final String LOCATION_RESOURCE_TYPE = "LOCATION";
  public static final String SERVICE_AREA_RESOURCE_TYPE = "SERVICE_AREA";
  public static final String COST_OPTION_RESOURCE_TYPE = "COST_OPTION";
  public static final String SERVICE_RESOURCE_TYPE = "SERVICE";
  public static final String REQUIRED_DOCUMENT_RESOURCE_TYPE = "REQUIRED_DOCUMENT";
  public static final String SERVICE_AT_LOCATION_RESOURCE_TYPE = "SERVICE_AT_LOCATION";
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
