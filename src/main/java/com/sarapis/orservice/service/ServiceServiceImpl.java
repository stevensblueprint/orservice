package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.repository.ServiceRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.serviceRepository = serviceRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        List<ServiceDTO> serviceDTOs = this.serviceRepository.findAll().stream()
                .map(com.sarapis.orservice.entity.core.Service::toDTO).toList();
        serviceDTOs.forEach(this::addRelatedData);
        return serviceDTOs;
    }

    @Override
    public ServiceDTO getServiceById(String serviceId) {
        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        ServiceDTO serviceDTO = service.toDTO();
        this.addRelatedData(serviceDTO);
        return serviceDTO;
    }

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        com.sarapis.orservice.entity.core.Service service = serviceDTO.toEntity(null, null);
        serviceDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(service.getId(), attributeDTO));
        serviceDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(service.getId(), e));

        com.sarapis.orservice.entity.core.Service createdService = this.serviceRepository.save(service);
        return this.getServiceById(createdService.getId());
    }

    @Override
    public ServiceDTO updateService(String serviceId, ServiceDTO serviceDTO) {
        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));

        com.sarapis.orservice.entity.core.Service updatedService = this.serviceRepository.save(service);
        return this.getServiceById(updatedService.getId());
    }

    @Override
    public void deleteService(String serviceId) {
        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found."));
        this.attributeService.deleteRelatedAttributes(service.getId());
        this.metadataService.deleteRelatedMetadata(service.getId());
        this.serviceRepository.delete(service);
    }

    private void addRelatedData(ServiceDTO serviceDTO) {
        serviceDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(serviceDTO.getId()));
        serviceDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(serviceDTO.getId()));
    }
}
