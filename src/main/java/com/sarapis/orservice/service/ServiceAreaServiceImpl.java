package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.entity.ServiceArea;
import com.sarapis.orservice.repository.ServiceAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAreaServiceImpl implements ServiceAreaService {
    private final ServiceAreaRepository serviceAreaRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ServiceAreaServiceImpl(ServiceAreaRepository serviceAreaRepository,
                                  AttributeService attributeService,
                                  MetadataService metadataService) {
        this.serviceAreaRepository = serviceAreaRepository;
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
        ServiceArea serviceArea = this.serviceAreaRepository.save(serviceAreaDTO.toEntity(null, null));
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
