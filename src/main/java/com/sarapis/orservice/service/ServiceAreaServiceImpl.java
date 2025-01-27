package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.entity.ServiceArea;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.ServiceAreaRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAreaServiceImpl implements ServiceAreaService {
    private final ServiceAreaRepository serviceAreaRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ServiceAreaServiceImpl(ServiceAreaRepository serviceAreaRepository,
                                  ServiceRepository serviceRepository,
                                  ServiceAtLocationRepository serviceAtLocationRepository,
                                  AttributeService attributeService,
                                  MetadataService metadataService) {
        this.serviceAreaRepository = serviceAreaRepository;
        this.serviceRepository = serviceRepository;
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ServiceAreaDTO> getAllServiceAreas() {
        List<ServiceAreaDTO> serviceAreaDTOs = this.serviceAreaRepository.findAll().stream().map(ServiceArea::toDTO)
                .toList();
        serviceAreaDTOs.forEach(this::addRelatedData);
        return serviceAreaDTOs;
    }

    @Override
    public ServiceAreaDTO getServiceAreaById(String serviceAreaId) {
        ServiceArea serviceArea = this.serviceAreaRepository.findById(serviceAreaId)
                .orElseThrow(() -> new RuntimeException("Service area not found."));
        ServiceAreaDTO serviceAreaDTO = serviceArea.toDTO();
        this.addRelatedData(serviceAreaDTO);
        return serviceAreaDTO;
    }

    @Override
    public ServiceAreaDTO createServiceArea(ServiceAreaDTO serviceAreaDTO) {
        com.sarapis.orservice.entity.core.Service service = null;
        ServiceAtLocation serviceAtLocation = null;

        if (serviceAreaDTO.getServiceId() != null) {
            service = this.serviceRepository.findById(serviceAreaDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
        }
        if (serviceAreaDTO.getServiceAtLocationId() != null) {
            serviceAtLocation = this.serviceAtLocationRepository.findById(serviceAreaDTO.getServiceAtLocationId())
                    .orElseThrow(() -> new RuntimeException("Service atlocation not found."));
        }

        ServiceArea serviceArea = this.serviceAreaRepository.save(serviceAreaDTO.toEntity(service, serviceAtLocation));
        serviceAreaDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(serviceArea.getId(), attributeDTO));
        serviceAreaDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(serviceArea.getId(), e));

        ServiceArea createdServiceArea = this.serviceAreaRepository.save(serviceArea);
        return this.getServiceAreaById(createdServiceArea.getId());
    }

    @Override
    public ServiceAreaDTO updateServiceArea(String serviceAreaId, ServiceAreaDTO serviceAreaDTO) {
        ServiceArea serviceArea = this.serviceAreaRepository.findById(serviceAreaId)
                .orElseThrow(() -> new RuntimeException("ServiceArea not found"));

        serviceArea.setId(serviceAreaDTO.getId());
        serviceArea.setName(serviceAreaDTO.getName());
        serviceArea.setDescription(serviceAreaDTO.getDescription());
        serviceArea.setExtent(serviceAreaDTO.getExtent());
        serviceArea.setExtentType(serviceAreaDTO.getExtentType());
        serviceArea.setUri(serviceAreaDTO.getUri());

        ServiceArea updatedServiceArea = this.serviceAreaRepository.save(serviceArea);
        return this.getServiceAreaById(updatedServiceArea.getId());
    }

    @Override
    public void deleteServiceArea(String serviceAreaId) {
        ServiceArea serviceArea = this.serviceAreaRepository.findById(serviceAreaId)
                .orElseThrow(() -> new RuntimeException("ServiceArea not found"));
        this.attributeService.deleteRelatedAttributes(serviceArea.getId());
        this.metadataService.deleteRelatedMetadata(serviceArea.getId());
        this.serviceAreaRepository.delete(serviceArea);
    }

    private void addRelatedData(ServiceAreaDTO serviceAreaDTO) {
        serviceAreaDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(serviceAreaDTO.getId()));
        serviceAreaDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(serviceAreaDTO.getId()));
    }
}
