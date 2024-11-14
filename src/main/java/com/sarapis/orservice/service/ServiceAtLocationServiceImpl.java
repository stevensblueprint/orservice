package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {
  private final ServiceAtLocationRepository serviceAtLocationRepository;

  @Autowired
  public ServiceAtLocationServiceImpl(ServiceAtLocationRepository serviceAtLocationService) {
    this.serviceAtLocationRepository = serviceAtLocationService;
  }

  private ServiceAtLocationDTO mapToDTO(ServiceAtLocation serviceAtLocation) {
    return null;
  }

  private ServiceAtLocation mapToEntity(ServiceAtLocationDTO serviceAtLocationDTO) {
    return null;
  }

  @Override
  public List<ServiceAtLocationDTO> getAllServicesAtLocation() {
    return List.of();
  }

  @Override
  public ServiceAtLocationDTO getServiceAtLocationById(String id) {
    return null;
  }

  @Override
  public ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO) {
    return null;
  }

  @Override
  public ServiceAtLocationDTO updateServiceAtLocation(String id,
      ServiceAtLocationDTO serviceAtLocationDTO) {
    return null;
  }

  @Override
  public void deleteServiceAtLocation(String id) {

  }
}
