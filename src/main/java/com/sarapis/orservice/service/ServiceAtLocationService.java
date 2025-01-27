package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import java.util.List;

public interface ServiceAtLocationService {
  List<ServiceAtLocationDTO> getAllServicesAtLocations(String search);

    ServiceAtLocationDTO getServiceAtLocationById(String serviceAtLocationId);

    ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO);

    ServiceAtLocationDTO updateServiceAtLocation(String serviceAtLocationId, ServiceAtLocationDTO serviceAtLocationDTO);

    void deleteServiceAtLocation(String serviceAtLocationId);
}
