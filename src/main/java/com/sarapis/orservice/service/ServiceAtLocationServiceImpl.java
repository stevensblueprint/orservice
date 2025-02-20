package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {

    private final ServiceAtLocationRepository serviceAtLocationRepository;

    @Autowired
    public ServiceAtLocationServiceImpl(ServiceAtLocationRepository serviceAtLocationRepository) {
        this.serviceAtLocationRepository = serviceAtLocationRepository;
    }

    @Override
    public List<ServiceAtLocationDTO> getAllServiceAtLocations() {
        return serviceAtLocationRepository.findAll().stream()
            .map(ServiceAtLocation::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ServiceAtLocationDTO getServiceAtLocationById(String id) {
        ServiceAtLocation serviceAtLocation = serviceAtLocationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ServiceAtLocation not found with id: " + id));
        return serviceAtLocation.toDTO();
    }

    @Override
    public ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO) {
        // Generate an ID if not provided.
        if (serviceAtLocationDTO.getId() == null || serviceAtLocationDTO.getId().isEmpty()) {
            serviceAtLocationDTO.setId(UUID.randomUUID().toString());
        }
        // Map the DTO to the entity. Note: Mapping for related entities (Service, Location, etc.) should be handled as needed.
        ServiceAtLocation serviceAtLocation = mapToEntity(serviceAtLocationDTO);
        ServiceAtLocation saved = serviceAtLocationRepository.save(serviceAtLocation);
        return saved.toDTO();
    }

    @Override
    public ServiceAtLocationDTO updateServiceAtLocation(String id, ServiceAtLocationDTO serviceAtLocationDTO) {
        ServiceAtLocation existing = serviceAtLocationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ServiceAtLocation not found with id: " + id));
        existing.setDescription(serviceAtLocationDTO.getDescription());
        // TODO: Update related entities (Service, Location, etc.) if required.
        ServiceAtLocation updated = serviceAtLocationRepository.save(existing);
        return updated.toDTO();
    }

    @Override
    public void deleteServiceAtLocation(String id) {
        ServiceAtLocation existing = serviceAtLocationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ServiceAtLocation not found with id: " + id));
        serviceAtLocationRepository.delete(existing);
    }

    private ServiceAtLocation mapToEntity(ServiceAtLocationDTO dto) {
        return ServiceAtLocation.builder()
            .id(dto.getId())
            .description(dto.getDescription())
            .build();
    }
}
