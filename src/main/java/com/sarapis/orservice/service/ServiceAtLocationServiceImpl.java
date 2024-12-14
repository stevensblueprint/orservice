package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.*;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {
    private final ServiceAtLocationRepository serviceAtLocationRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public ServiceAtLocationServiceImpl(ServiceAtLocationRepository serviceAtLocationService, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.serviceAtLocationRepository = serviceAtLocationService;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<ServiceAtLocationDTO> getAllServicesAtLocations(String search) {
        List<ServiceAtLocationDTO> serviceAtLocationDTOs = this.serviceAtLocationRepository.getAllServiceAtLocations(search).stream().map(ServiceAtLocation::toDTO).toList();
        serviceAtLocationDTOs.forEach(this::addRelatedData);
        return serviceAtLocationDTOs;
    }

    @Override
    public ServiceAtLocationDTO getServiceAtLocationById(String id) {
        ServiceAtLocation servLoc = this.serviceAtLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service At Location not found."));

        ServiceAtLocationDTO servLocDTO = servLoc.toDTO();
        this.addRelatedData(servLocDTO);
        return servLocDTO;
    }

    @Override
    public ServiceAtLocationDTO createServiceAtLocation(ServiceAtLocationDTO serviceAtLocationDTO) {
        ServiceAtLocation servLoc = this.serviceAtLocationRepository.save(serviceAtLocationDTO.toEntity());

        for (AttributeDTO attributeDTO : serviceAtLocationDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(servLoc.getId()));
        }

        for (MetadataDTO metadataDTO : serviceAtLocationDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(servLoc.getId()));
        }

        ServiceAtLocationDTO savedServLocDTO = this.serviceAtLocationRepository.save(servLoc).toDTO();
        this.addRelatedData(savedServLocDTO);
        return savedServLocDTO;
    }

    @Override
    public ServiceAtLocationDTO updateServiceAtLocation(String id,
                                                        ServiceAtLocationDTO serviceAtLocationDTO) {
        ServiceAtLocation servLoc = this.serviceAtLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service At Location not found"));

        servLoc.setDescription(serviceAtLocationDTO.getDescription());
        servLoc.setServiceAreas(serviceAtLocationDTO.getServiceAreas().stream().map(ServiceAreaDTO::toEntity).toList());
        servLoc.setContacts(serviceAtLocationDTO.getContacts().stream().map(ContactDTO::toEntity).toList());
        servLoc.setPhones(serviceAtLocationDTO.getPhones().stream().map(PhoneDTO::toEntity).toList());
        servLoc.setSchedules(serviceAtLocationDTO.getSchedules().stream().map(ScheduleDTO::toEntity).toList());
        servLoc.setLocation(serviceAtLocationDTO.getLocation().toEntity());

        ServiceAtLocation updatedServLoc = this.serviceAtLocationRepository.save(servLoc);
        return updatedServLoc.toDTO();
    }

    @Override
    public void deleteServiceAtLocation(String id) {
        ServiceAtLocation servLoc = this.serviceAtLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service At Location not found."));

        this.serviceAtLocationRepository.delete(servLoc);
    }

    private void addRelatedData(ServiceAtLocationDTO serviceAtLocationDTO) {
        serviceAtLocationDTO.getAttributes().addAll(this.serviceAtLocationRepository.getAttributes(serviceAtLocationDTO.getId()).stream().map(Attribute::toDTO).toList());
        serviceAtLocationDTO.getMetadata().addAll(this.serviceAtLocationRepository.getMetadata(serviceAtLocationDTO.getId()).stream().map(Metadata::toDTO).toList());
    }
}
