package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.repository.OrganizationRepository;
import com.sarapis.orservice.repository.ProgramRepository;
import com.sarapis.orservice.repository.ServiceRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final OrganizationRepository organizationRepository;
    private final ProgramRepository programRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              OrganizationRepository organizationRepository,
                              ProgramRepository programRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.serviceRepository = serviceRepository;
        this.organizationRepository = organizationRepository;
        this.programRepository = programRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<ServiceDTO> getAllServices(String search) {
        List<ServiceDTO> serviceDTOs = this.serviceRepository.getAllServices(search)
                .stream().map(com.sarapis.orservice.entity.core.Service::toDTO).toList();
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
        Organization organization = null;
        Program program = null;

        if (serviceDTO.getOrganizationId() != null) {
            organization = this.organizationRepository.findById(serviceDTO.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found."));
        }
        if (serviceDTO.getProgramId() != null) {
            program = this.programRepository.findById(serviceDTO.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found."));
        }

        com.sarapis.orservice.entity.core.Service service = serviceDTO.toEntity(organization, program);
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
