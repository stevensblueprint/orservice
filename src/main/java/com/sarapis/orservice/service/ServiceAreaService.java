package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import java.util.List;

public interface ServiceAreaService {
  ServiceAreaDTO.Response createServiceArea(ServiceAreaDTO.Request dto);
  List<ServiceAreaDTO.Response> getServiceAreasByServiceId(String serviceId);
}
