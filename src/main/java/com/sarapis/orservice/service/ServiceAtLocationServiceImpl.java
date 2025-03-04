package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Request;
import com.sarapis.orservice.dto.ServiceAtLocationDTO.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllServicesAtLocation(String search, String taxonomyTermId,
      String taxonomyId, String organizationId, String modifiedAfter, Boolean full, Integer page,
      Integer perPage, String format, String postcode, String proximity) {
    return null;
  }

  @Override
  @Transactional
  public Response createServiceAtLocation(Request dto) {
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public Response getServiceAtLocationById(String id) {
    return null;
  }
}
