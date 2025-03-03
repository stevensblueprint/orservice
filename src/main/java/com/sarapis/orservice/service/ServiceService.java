package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import java.util.List;

public interface ServiceService {
  PaginationDTO<ServiceDTO.Response> getAllServices(
      String search,
      Integer page,
      Integer perPage,
      String format,
      String taxonomyTermId,
      String taxonomyId,
      String organizationId,
      String modifiedAfter,
      Boolean minimal,
      Boolean full
  );
  ServiceDTO.Response getServiceById(String id);
  ServiceDTO.Response createService(ServiceDTO.Request requestDto);
}
