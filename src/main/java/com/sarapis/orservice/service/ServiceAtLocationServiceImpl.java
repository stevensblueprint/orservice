package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.dto.upsert.UpsertServiceAtLocationDTO;
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
    public ServiceAtLocationDTO createServiceAtLocation(UpsertServiceAtLocationDTO upsertServiceAtLocationDTO) {
        ServiceAtLocation serviceAtLocation = upsertServiceAtLocationDTO.create();

        com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(upsertServiceAtLocationDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found."));
        serviceAtLocation.setService(service);

        Location location = this.locationRepository.findById(upsertServiceAtLocationDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found."));
        serviceAtLocation.setLocation(location);

        ServiceAtLocation createdServiceAtLocation = this.serviceAtLocationRepository.save(serviceAtLocation);
        service.getServiceAtLocations().add(createdServiceAtLocation);
        location.getServiceAtLocations().add(createdServiceAtLocation);
        this.serviceRepository.save(service);
        this.locationRepository.save(location);
        return this.getServiceAtLocationById(createdServiceAtLocation.getId());
    }

    @Override
    public ServiceAtLocationDTO updateServiceAtLocation(String serviceAtLocationId, UpsertServiceAtLocationDTO upsertServiceAtLocationDTO) {
        ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(serviceAtLocationId)
                .orElseThrow(() -> new RuntimeException("Service at location not found"));
        ServiceAtLocation updatedServiceAtLocation = upsertServiceAtLocationDTO.merge(serviceAtLocation);
        updatedServiceAtLocation.setId(serviceAtLocationId);

        if (upsertServiceAtLocationDTO.getServiceId() != null) {
            com.sarapis.orservice.entity.core.Service service = this.serviceRepository.findById(upsertServiceAtLocationDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found."));
            service.getServiceAtLocations().add(updatedServiceAtLocation);
            this.serviceRepository.save(service);
            serviceAtLocation.getService().getServiceAtLocations().remove(serviceAtLocation);
            updatedServiceAtLocation.setService(service);
        }

        if (upsertServiceAtLocationDTO.getLocationId() != null) {
            Location location = this.locationRepository.findById(upsertServiceAtLocationDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found."));
            location.getServiceAtLocations().add(updatedServiceAtLocation);
            this.locationRepository.save(location);
            serviceAtLocation.getLocation().getServiceAtLocations().remove(serviceAtLocation);
            updatedServiceAtLocation.setLocation(location);
        }

        this.serviceAtLocationRepository.save(updatedServiceAtLocation);
        return this.getServiceAtLocationById(updatedServiceAtLocation.getId());
    }

    @Override
    public void deleteServiceAtLocation(String serviceAtLocationId) {
        ServiceAtLocation serviceAtLocation = this.serviceAtLocationRepository.findById(serviceAtLocationId)
                .orElseThrow(() -> new RuntimeException("Service at location not found."));
        this.attributeService.deleteAttribute(serviceAtLocation.getId());
        this.metadataService.deleteMetadata(serviceAtLocation.getId());
        this.serviceAtLocationRepository.delete(serviceAtLocation);
    }

    private void addRelatedData(ServiceAtLocationDTO serviceAtLocationDTO) {
        serviceAtLocationDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(serviceAtLocationDTO.getId()));
        serviceAtLocationDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(serviceAtLocationDTO.getId()));
    }
}
