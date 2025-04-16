package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.model.Metadata;

import java.util.List;
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
  ServiceDTO.Response updateService(String id, ServiceDTO.Request updatedDto, String updatedBy);
  ServiceDTO.Response undoServiceMetadata(String metadataId, String updatedBy);
  ServiceDTO.Response undoServiceMetadataBatch(List<Metadata> metadataList, String updatedBy);
}
