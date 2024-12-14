package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {
  private final ServiceRepository serviceRepository;
  private final AttributeRepository attributeRepository;
  private final MetadataRepository metadataRepository;

  @Autowired
  public ServiceServiceImpl(ServiceRepository serviceRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
    this.serviceRepository = serviceRepository;
    this.attributeRepository = attributeRepository;
    this.metadataRepository = metadataRepository;
  }

  @Override
  public List<ServiceDTO> getAllServices(String search) {
    List<ServiceDTO> serviceDTOs = this.serviceRepository.getAllServices(search).stream().map(com.sarapis.orservice.entity.core.Service::toDTO).toList();
    serviceDTOs.forEach(this::addRelatedData);
    return serviceDTOs;
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

  private void addRelatedData(ServiceDTO serviceDTO) {
    serviceDTO.getAttributes().addAll(this.serviceRepository.getAttributes(serviceDTO.getId()).stream().map(Attribute::toDTO).toList());
    serviceDTO.getMetadata().addAll(this.serviceRepository.getMetadata(serviceDTO.getId()).stream().map(Metadata::toDTO).toList());
  }
}
