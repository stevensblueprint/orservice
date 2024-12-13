package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.dto.ServiceAreaDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.ServiceAtLocationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public List<ServiceAtLocationDTO> getAllServicesAtLocation() {
    List<ServiceAtLocationDTO> servLocsDTOs = this.serviceAtLocationRepository.findAll().stream()
            .map(ServiceAtLocation::toDTO)
            .toList();
    servLocsDTOs.forEach(this::addRelatedData);
    return servLocsDTOs;
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
    String id = serviceAtLocationDTO.getId();
    List<AttributeDTO> attributeDTOs = this.serviceAtLocationRepository.getAttributes(id)
            .stream()
            .map(Attribute::toDTO)
            .toList();

    List<MetadataDTO> metadataDTOs = this.serviceAtLocationRepository.getMetadata(id)
            .stream()
            .map(Metadata::toDTO)
            .toList();

    serviceAtLocationDTO.getAttributes().addAll(attributeDTOs);
    serviceAtLocationDTO.getMetadata().addAll(metadataDTOs);
  }
}
