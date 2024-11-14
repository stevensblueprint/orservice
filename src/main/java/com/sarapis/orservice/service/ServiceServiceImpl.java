package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.repository.ServiceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceServiceImpl implements ServiceService {
  private final ServiceRepository serviceRepository;

  @Autowired
  public ServiceServiceImpl(ServiceRepository serviceRepository) {
    this.serviceRepository = serviceRepository;
  }

  private ServiceDTO mapToDTO(ServiceDTO serviceDTO) {
    return null;
  }

  private com.sarapis.orservice.entity.core.Service mapToEntity(ServiceDTO serviceDTO) {
    return null;
  }

  @Override
  public List<ServiceDTO> getAllServices() {
    return List.of();
  }

  @Override
  public ServiceDTO getServiceById(String id) {
    return null;
  }

  @Override
  public ServiceDTO createService(ServiceDTO serviceDTO) {
    return null;
  }

  @Override
  public ServiceDTO updateService(String id, ServiceDTO serviceDTO) {
    return null;
  }

  @Override
  public void deleteService(String id) {

  }
}
