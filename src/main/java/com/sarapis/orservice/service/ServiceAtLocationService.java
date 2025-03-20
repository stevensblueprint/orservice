package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import java.util.List;

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
      String format,
      String postcode,
      String proximity
  );
  ServiceAtLocationDTO.Response createServiceAtLocation(ServiceAtLocationDTO.Request dto, String updatedBy);
  ServiceAtLocationDTO.Response getServiceAtLocationById(String id);

}
