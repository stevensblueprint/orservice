package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;

import java.util.function.Consumer;

public interface ServiceAtLocationService {
  PaginationDTO<ServiceAtLocationDTO.Response> getAllServicesAtLocation(
      String search,
      String taxonomyTermId,
      String taxonomyId,
      String organizationId,
      String modifiedAfter,
      Boolean full,
      Integer page,
      Integer perPage,
      String postcode,
      String proximity
  );
  void streamAllServicesAtLocation(String search, String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean full, String postcode, String proximity, Consumer<ServiceAtLocationDTO.Response> consumer);

  ServiceAtLocationDTO.Response createServiceAtLocation(ServiceAtLocationDTO.Request dto, String updatedBy);
  ServiceAtLocationDTO.Response getServiceAtLocationById(String id);
  ServiceAtLocationDTO.Response undoServiceAtLocationMetadata(String metadataId);
}
