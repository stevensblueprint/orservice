package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import java.util.List;

public interface ServiceAtLocationService {
  List<ServiceAtLocationDTO> getAllServicesAtLocation();

  ServiceAtLocationDTO getServiceAtLocationById(Long id);

  ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO);

  ServiceAtLocationDTO updateServiceAtLocation(Long id, ServiceAtLocationDTO serviceAtLocationDTO);

  void deleteServiceAtLocation(Long id);
}
