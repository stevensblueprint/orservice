package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.LocationDTO;
import com.sarapis.orservice.model.Metadata;

import java.util.List;

public interface LocationService extends Exchangeable {
  LocationDTO.Response createLocation(LocationDTO.Request requestDto, String updatedBy);
  LocationDTO.Response undoLocationMetadata(String metadataId, String updatedBy);
  LocationDTO.Response undoLocationMetadataBatch(List<Metadata> metadataList, String updatedBy);
}
