package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.LocationRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import com.sarapis.orservice.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final ServiceRepository serviceRepository;
    private final LocationRepository locationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ServiceAtLocationServiceImpl(ServiceAtLocationRepository serviceAtLocationService,
                                        ServiceRepository serviceRepository,
                                        LocationRepository locationRepository,
                                        AttributeService attributeService,
                                        MetadataService metadataService) {
        this.serviceAtLocationRepository = serviceAtLocationService;
        this.serviceRepository = serviceRepository;
        this.locationRepository = locationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ServiceAtLocationDTO> getAllServicesAtLocation() {
        List<ServiceAtLocationDTO> serviceAtLocationDTOs = this.serviceAtLocationRepository.findAll().stream()
                .map(ServiceAtLocation::toDTO).toList();
        serviceAtLocationDTOs.forEach(this::addRelatedData);
        return serviceAtLocationDTOs;
    }

    @Override
    public ServiceAtLocationDTO getServiceAtLocationById(String serviceAtLocationId) {
        ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(serviceAtLocationId)
                .orElseThrow(() -> new RuntimeException("Service at location not found."));
        ServiceAtLocationDTO servLocDTO = serviceAtLocation.toDTO();
        this.addRelatedData(servLocDTO);
        return servLocDTO;
    }

    @Override
    public ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO) {
        com.sarapis.orservice.entity.core.Service service = null;
        Location location = null;

        if (serviceAtLocationDTO.getServiceId() != null) {
            service = this.serviceRepository.findById(serviceAtLocationDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
        }
        if (serviceAtLocationDTO.getLocationId() != null) {
            location = this.locationRepository.findById(serviceAtLocationDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Service at location not found."));
        }

        ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.save(serviceAtLocationDTO.toEntity(service, location));
        serviceAtLocationDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(serviceAtLocation.getId(), attributeDTO));
        serviceAtLocationDTO.getMetadata()
                .forEach(e -> this.metadataService.createMetadata(serviceAtLocation.getId(), e));

        ServiceAtLocation createdServiceAtLocation = this.serviceAtLocationRepository.save(serviceAtLocation);
        return this.getServiceAtLocationById(createdServiceAtLocation.getId());
    }

    @Override
    public ServiceAtLocationDTO updateServiceAtLocation(String serviceAtLocationId, ServiceAtLocationDTO serviceAtLocationDTO) {
        ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(serviceAtLocationId)
                .orElseThrow(() -> new RuntimeException("Service At Location not found"));

        serviceAtLocation.setDescription(serviceAtLocationDTO.getDescription());

        ServiceAtLocation updatedServiceAtLocation = this.serviceAtLocationRepository.save(serviceAtLocation);
        return this.getServiceAtLocationById(updatedServiceAtLocation.getId());
    }

    @Override
    public void deleteServiceAtLocation(String serviceAtLocationId) {
        ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(serviceAtLocationId)
                .orElseThrow(() -> new RuntimeException("Service At Location not found."));
        this.attributeService.deleteAttribute(serviceAtLocation.getId());
        this.metadataService.deleteMetadata(serviceAtLocation.getId());
        this.serviceAtLocationRepository.delete(serviceAtLocation);
    }

    private void addRelatedData(ServiceAtLocationDTO serviceAtLocationDTO) {
        serviceAtLocationDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(serviceAtLocationDTO.getId()));
        serviceAtLocationDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(serviceAtLocationDTO.getId()));
    }
}
