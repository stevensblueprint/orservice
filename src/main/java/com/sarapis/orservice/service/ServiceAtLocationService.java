package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import java.util.List;

public interface ServiceAtLocationService {
  List<ServiceAtLocationDTO> getAllServicesAtLocations(String search);

  ServiceAtLocationDTO getServiceAtLocationById(String id);

  ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO);

  ServiceAtLocationDTO updateServiceAtLocation(String id, ServiceAtLocationDTO serviceAtLocationDTO);

  void deleteServiceAtLocation(String id);
}
