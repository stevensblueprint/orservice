package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import java.util.List;

public interface ServiceService {
  List<ServiceDTO> getAllServices();

  ServiceDTO getServiceById(String id);

  ServiceDTO createService(ServiceDTO serviceDTO);

  ServiceDTO updateService(String id, ServiceDTO serviceDTO);

  void deleteService(String id);
}
