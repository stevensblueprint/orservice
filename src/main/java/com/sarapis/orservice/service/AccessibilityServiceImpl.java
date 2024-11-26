package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.entity.Accessibility;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.AccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccessibilityServiceImpl implements AccessibilityService {

    private final AccessibilityRepository accessibilityRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public AccessibilityServiceImpl(AccessibilityRepository accessibilityRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.accessibilityRepository = accessibilityRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<AccessibilityDTO> getAllAccessibilities() {
        return accessibilityRepository.findAll().stream().map(Accessibility::toDTO).toList();
    }

    @Override
    public AccessibilityDTO getAccessibilityById(String id) {
        Accessibility accessibility = accessibilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Accessibility not found"));
        return accessibility.toDTO();
    }

    @Override
    public AccessibilityDTO createAccessibility(AccessibilityDTO accessibilityDTO) {
        Accessibility accessibility = accessibilityDTO.toEntity();

        for (AttributeDTO attributeDTO : accessibilityDTO.getAttributes()) {
            attributeRepository.save(attributeDTO.toEntity(accessibility.getId()));
        }

        for (MetadataDTO metadataDTO : accessibilityDTO.getMetadata()) {
            metadataRepository.save(metadataDTO.toEntity(accessibility.getId()));
        }

        Accessibility savedAccessibility = accessibilityRepository.save(accessibility);
        return savedAccessibility.toDTO();
    }

    @Override
    public AccessibilityDTO updateAccessibility(String id, AccessibilityDTO accessibilityDTO) {
        Accessibility oldAccessibility = accessibilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Accessibility not found"));

        oldAccessibility.setDescription(accessibilityDTO.getDescription());
        oldAccessibility.setDetails(accessibilityDTO.getDetails());
        oldAccessibility.setUrl(accessibilityDTO.getUrl());

        Accessibility updatedAccessibility = accessibilityRepository.save(oldAccessibility);
        return updatedAccessibility.toDTO();
    }

    @Override
    public void deleteAccessibility(String id) {
        Accessibility accessibility = accessibilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Accessibility not found"));

        List<Attribute> attributes = attributeRepository.findAll();
        List<Attribute> relatedAttributes = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), accessibility.getId())).toList();
        attributeRepository.deleteAll(relatedAttributes);

        List<Metadata> metadatas = metadataRepository.findAll();
        List<Metadata> relatedMetadatas = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), accessibility.getId())).toList();
        metadataRepository.deleteAll(relatedMetadatas);

        accessibilityRepository.delete(accessibility);
    }
}