package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import java.util.function.Consumer;

public interface ServiceService extends Exchangeable {
  PaginationDTO<ServiceDTO.Response> getAllServices(
      String search,
      Integer page,
      Integer perPage,
      String taxonomyTermId,
      String taxonomyId,
      String organizationId,
      String modifiedAfter,
      Boolean minimal,
      Boolean full
  );
  void streamAllServices(String search, String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean minimal, Boolean full, Consumer<ServiceDTO.Response> consumer);
  ServiceDTO.Response getServiceById(String id);
  ServiceDTO.Response createService(ServiceDTO.Request requestDto, String updatedBy);
  ServiceDTO.Response undoServiceMetadata(String metadataId, String updatedBy);
}
