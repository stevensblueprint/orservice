package com.sarapis.orservice.service;


import com.sarapis.orservice.dto.UrlDTO;
import java.util.List;

public interface UrlService {
  UrlDTO.Response createUrl(UrlDTO.Request url);
  List<UrlDTO.Response> getUrlsByOrganizationId(String organizationId);
}
