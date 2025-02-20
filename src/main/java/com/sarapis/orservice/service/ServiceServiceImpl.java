package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream()
            .map(com.sarapis.orservice.entity.core.Service::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ServiceDTO getServiceById(String id) {
        com.sarapis.orservice.entity.core.Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return service.toDTO();
    }

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        // Generate a new ID if not provided.
        if (serviceDTO.getId() == null || serviceDTO.getId().isEmpty()) {
            serviceDTO.setId(UUID.randomUUID().toString());
        }
        com.sarapis.orservice.entity.core.Service service = mapToEntity(serviceDTO);
        com.sarapis.orservice.entity.core.Service saved = serviceRepository.save(service);
        return saved.toDTO();
    }

    @Override
    public ServiceDTO updateService(String id, ServiceDTO serviceDTO) {
        com.sarapis.orservice.entity.core.Service existing = serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        existing.setName(serviceDTO.getName());
        existing.setAlternateName(serviceDTO.getAlternateName());
        existing.setDescription(serviceDTO.getDescription());
        existing.setUrl(serviceDTO.getUrl());
        existing.setEmail(serviceDTO.getEmail());
        existing.setStatus(serviceDTO.getStatus());
        existing.setInterpretationServices(serviceDTO.getInterpretationServices());
        existing.setApplicationProcess(serviceDTO.getApplicationProcess());
        existing.setFeesDescription(serviceDTO.getFeesDescription());
        existing.setWaitTime(serviceDTO.getWaitTime());
        existing.setFees(serviceDTO.getFees());
        existing.setAccreditations(serviceDTO.getAccreditations());
        existing.setEligibilityDescription(serviceDTO.getEligibilityDescription());
        existing.setMinimumAge(serviceDTO.getMinimumAge());
        existing.setMaximumAge(serviceDTO.getMaximumAge());
        existing.setAssuredDate(serviceDTO.getAssuredDate());
        existing.setAssurerEmail(serviceDTO.getAssurerEmail());
        existing.setLicenses(serviceDTO.getLicenses());
        existing.setAlert(serviceDTO.getAlert());
        existing.setLastModified(serviceDTO.getLastModified());
        // TODO: Handle updates for collections and related entities.
        com.sarapis.orservice.entity.core.Service updated = serviceRepository.save(existing);
        return updated.toDTO();
    }

    @Override
    public void deleteService(String id) {
        com.sarapis.orservice.entity.core.Service existing = serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        serviceRepository.delete(existing);
    }

    // Helper method for mapping DTO to entity. Extend this as needed to set related entities.
    private com.sarapis.orservice.entity.core.Service mapToEntity(ServiceDTO dto) {
        return com.sarapis.orservice.entity.core.Service.builder()
            .id(dto.getId())
            .name(dto.getName())
            .alternateName(dto.getAlternateName())
            .description(dto.getDescription())
            .url(dto.getUrl())
            .email(dto.getEmail())
            .status(dto.getStatus())
            .interpretationServices(dto.getInterpretationServices())
            .applicationProcess(dto.getApplicationProcess())
            .feesDescription(dto.getFeesDescription())
            .waitTime(dto.getWaitTime())
            .fees(dto.getFees())
            .accreditations(dto.getAccreditations())
            .eligibilityDescription(dto.getEligibilityDescription())
            .minimumAge(dto.getMinimumAge())
            .maximumAge(dto.getMaximumAge())
            .assuredDate(dto.getAssuredDate())
            .assurerEmail(dto.getAssurerEmail())
            .licenses(dto.getLicenses())
            .alert(dto.getAlert())
            .lastModified(dto.getLastModified())
            // TODO: Map collections and related entities as required.
            .build();
    }
}
