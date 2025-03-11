package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.MetadataDTO.Response;
import com.sarapis.orservice.utils.Metadata;
import java.util.List;

public interface MetadataService {
  List<Response> getMetadataByResourceIdAndResourceType(String resourceId, String resourceType);
  <T> List<com.sarapis.orservice.model.Metadata> createMetadata(T original, T updated, String resourceType, Metadata actionType, String updatedBy);
}
