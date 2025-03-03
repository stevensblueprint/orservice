package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.dto.UrlDTO.Response;
import java.util.List;

public interface UrlService {
  List<Response> getAllUrls();
  UrlDTO.Response getUrlById(String id);
  UrlDTO.Response createUrl(UrlDTO.Request requestDto, String updatedBy);
  UrlDTO.Response updateUrl(String id, UrlDTO.UpdateRequest updateDto, String updatedBy);
  void deleteUrl(String id, String deletedBy);
  List<UrlDTO.Response> getUrlsByOrganizationId(String organizationId);
  List<UrlDTO.Response> getUrlsByServiceId(String serviceId);
}
