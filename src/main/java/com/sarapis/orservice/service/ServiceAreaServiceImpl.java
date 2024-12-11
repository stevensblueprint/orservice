package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.ServiceArea;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAreaServiceImpl implements ServiceAreaService{
    private final ServiceAreaRepository serviceAreaRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public ServiceAreaServiceImpl(ServiceAreaRepository serviceAreaRepository,
                                  AttributeRepository attributeRepository,
                                  MetadataRepository metadataRepository) {
        this.serviceAreaRepository = serviceAreaRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<ServiceAreaDTO> getAllServiceAreas() {
        List<ServiceAreaDTO> serviceAreaDTOs = this.serviceAreaRepository.findAll().stream().map(ServiceArea::toDTO).toList();
        serviceAreaDTOs.forEach(this::addRelatedData);
        return serviceAreaDTOs;
    }

    @Override
    public ServiceAreaDTO getServiceAreaById(String id) {
        ServiceArea serviceArea = this.serviceAreaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Area not found."));
        ServiceAreaDTO serviceAreaDTO = serviceArea.toDTO();
        this.addRelatedData(serviceAreaDTO);
        return serviceAreaDTO;
    }

    @Override
    public ServiceAreaDTO createServiceArea(ServiceAreaDTO serviceAreaDTO) {
        ServiceArea serviceArea = this.serviceAreaRepository.save(serviceAreaDTO.toEntity());

        for(AttributeDTO attributeDTO: serviceAreaDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(serviceArea.getId()));
        }

        for (MetadataDTO metadataDTO : serviceAreaDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(serviceArea.getId()));
        }

        ServiceAreaDTO savedServiceAreaDTO = this.serviceAreaRepository.save(serviceArea).toDTO();
        this.addRelatedData(serviceAreaDTO);
        return savedServiceAreaDTO;
    }

    @Override
    public ServiceAreaDTO updateServiceArea(String id, ServiceAreaDTO serviceAreaDTO) {
        ServiceArea oldServiceArea = this.serviceAreaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceArea not found"));
        oldServiceArea.setId(serviceAreaDTO.getId());
        oldServiceArea.setName(serviceAreaDTO.getName());
        oldServiceArea.setDescription(serviceAreaDTO.getDescription());
        oldServiceArea.setExtent(serviceAreaDTO.getExtent());
        oldServiceArea.setExtentType(serviceAreaDTO.getExtentType());
        oldServiceArea.setUri(serviceAreaDTO.getUri());

        ServiceArea updatedServiceArea = this.serviceAreaRepository.save(oldServiceArea);
        return updatedServiceArea.toDTO();
    }

    @Override
    public void deleteServiceArea(String id) {
        ServiceArea serviceArea = this.serviceAreaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceArea not found"));
        this.serviceAreaRepository.deleteAttributes(serviceArea.getId());
        this.serviceAreaRepository.deleteMetadata(serviceArea.getId());
        this.serviceAreaRepository.delete(serviceArea);
    }

    private void addRelatedData(ServiceAreaDTO serviceAreaDTO) {
        serviceAreaDTO.getAttributes().addAll(this.serviceAreaRepository.getAttributes(serviceAreaDTO.getId()).stream().map(Attribute::toDTO).toList());
        serviceAreaDTO.getMetadata().addAll(this.serviceAreaRepository.getMetadata(serviceAreaDTO.getId()).stream().map(Metadata::toDTO).toList());
    }
}
