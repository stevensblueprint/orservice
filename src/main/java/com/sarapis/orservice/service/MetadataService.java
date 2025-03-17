package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.MetadataDTO.Response;
import java.util.List;

public interface MetadataService {
  List<Response> getMetadataByResourceIdAndResourceType(String resourceId, String resourceType);
  MetadataDTO.Response createMetadata(MetadataDTO.Request requestDto);
  void undoMetadata(String metadataId);
}
