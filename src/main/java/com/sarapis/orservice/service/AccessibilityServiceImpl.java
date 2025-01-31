package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.entity.Accessibility;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.exception.ResourceNotFoundException;
import com.sarapis.orservice.repository.AccessibilityRepository;
import com.sarapis.orservice.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessibilityServiceImpl implements AccessibilityService {
    private final AccessibilityRepository accessibilityRepository;
    private final LocationRepository locationRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public AccessibilityServiceImpl(AccessibilityRepository accessibilityRepository,
                                    LocationRepository locationRepository,
                                    AttributeService attributeService,
                                    MetadataService metadataService) {
        this.accessibilityRepository = accessibilityRepository;
        this.locationRepository = locationRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<AccessibilityDTO> getAllAccessibilities() {
        List<AccessibilityDTO> accessibilityDTOs = this.accessibilityRepository.findAll()
                .stream().map(Accessibility::toDTO).toList();
        accessibilityDTOs.forEach(this::addRelatedData);
        return accessibilityDTOs;
    }

    @Override
    public AccessibilityDTO getAccessibilityById(String accessibilityId) {
        Accessibility accessibility = this.accessibilityRepository.findById(accessibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Accessibility not found."));
        AccessibilityDTO accessibilityDTO = accessibility.toDTO();
        this.addRelatedData(accessibilityDTO);
        return accessibilityDTO;
    }

    @Override
    public AccessibilityDTO createAccessibility(AccessibilityDTO accessibilityDTO) {
        Location location = null;

        if (accessibilityDTO.getLocationId() != null) {
            location = this.locationRepository.findById(accessibilityDTO.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found."));
        }

        Accessibility accessibility = this.accessibilityRepository.save(accessibilityDTO.toEntity(location));
        accessibilityDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(accessibility.getId(), attributeDTO));
        accessibilityDTO.getMetadata()
                .forEach(e -> this.metadataService.createMetadata(accessibility.getId(), e));

        Accessibility createdAccessibility = this.accessibilityRepository.save(accessibility);
        return this.getAccessibilityById(createdAccessibility.getId());
    }

    @Override
    public AccessibilityDTO updateAccessibility(String accessibilityId, AccessibilityDTO accessibilityDTO) {
        Accessibility accessibility = this.accessibilityRepository.findById(accessibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Accessibility not found."));

        accessibility.setDescription(accessibilityDTO.getDescription());
        accessibility.setDetails(accessibilityDTO.getDetails());
        accessibility.setUrl(accessibilityDTO.getUrl());

        Accessibility updatedAccessibility = this.accessibilityRepository.save(accessibility);
        return this.getAccessibilityById(updatedAccessibility.getId());
    }

    @Override
    public void deleteAccessibility(String accessibilityId) {
        Accessibility accessibility = this.accessibilityRepository.findById(accessibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Accessibility not found."));
        this.attributeService.deleteRelatedAttributes(accessibility.getId());
        this.metadataService.deleteRelatedMetadata(accessibility.getId());
        this.accessibilityRepository.delete(accessibility);
    }

    private void addRelatedData(AccessibilityDTO accessibilityDTO) {
        accessibilityDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(accessibilityDTO.getId()));
        accessibilityDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(accessibilityDTO.getId()));
    }
}