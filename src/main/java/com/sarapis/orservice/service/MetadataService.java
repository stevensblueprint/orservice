package com.sarapis.orservice.service;

public interface MetadataService {
  void createInitialMetadata(String resourceId, String resourceType, String fieldName, String value, String updatedBy);
  void createMetadata(String resourceId, String resourceType, String fieldName, String previousValue,
      String replacementValue, String updatedBy);
}
