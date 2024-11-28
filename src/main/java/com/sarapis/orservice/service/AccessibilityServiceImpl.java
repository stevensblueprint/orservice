package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Accessibility;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AccessibilityRepository;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
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
        List<AccessibilityDTO> accessibilityDTOs = this.accessibilityRepository.findAll().stream().map(Accessibility::toDTO).toList();
        accessibilityDTOs.forEach(this::getRelatedData);
        return accessibilityDTOs;
    }

    @Override
    public AccessibilityDTO getAccessibilityById(String id) {
        Accessibility accessibility = this.accessibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessibility not found."));
        AccessibilityDTO accessibilityDTO = accessibility.toDTO();
        getRelatedData(accessibilityDTO);
        return accessibilityDTO;
    }

    @Override
    public AccessibilityDTO createAccessibility(AccessibilityDTO accessibilityDTO) {
        Accessibility accessibility = this.accessibilityRepository.save(accessibilityDTO.toEntity());

        for (AttributeDTO attributeDTO : accessibilityDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(accessibility.getId()));
        }

        for (MetadataDTO metadataDTO : accessibilityDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(accessibility.getId()));
        }

        AccessibilityDTO savedAccessibilityDTO = this.accessibilityRepository.save(accessibility).toDTO();
        getRelatedData(savedAccessibilityDTO);
        return savedAccessibilityDTO;
    }

    @Override
    public AccessibilityDTO updateAccessibility(String id, AccessibilityDTO accessibilityDTO) {
        Accessibility oldAccessibility = this.accessibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessibility not found."));

        oldAccessibility.setDescription(accessibilityDTO.getDescription());
        oldAccessibility.setDetails(accessibilityDTO.getDetails());
        oldAccessibility.setUrl(accessibilityDTO.getUrl());

        Accessibility updatedAccessibility = this.accessibilityRepository.save(oldAccessibility);
        return updatedAccessibility.toDTO();
    }

    @Override
    public void deleteAccessibility(String id) {
        Accessibility accessibility = this.accessibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessibility not found."));

        List<Attribute> attributes = this.attributeRepository.findAll();
        List<Attribute> relatedAttributes = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), accessibility.getId())).toList();
        this.attributeRepository.deleteAll(relatedAttributes);

        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<Metadata> relatedMetadatas = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), accessibility.getId())).toList();
        this.metadataRepository.deleteAll(relatedMetadatas);

        this.accessibilityRepository.delete(accessibility);
    }

    private void getRelatedData(AccessibilityDTO accessibilityDTO) {
        List<Attribute> attributes = this.attributeRepository.findAll();
        List<AttributeDTO> relatedAttributeDTOs = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), accessibilityDTO.getId())).map(Attribute::toDTO).toList();

        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<MetadataDTO> relatedMetadataDTOs = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), accessibilityDTO.getId())).map(Metadata::toDTO).toList();

        accessibilityDTO.getAttributes().addAll(relatedAttributeDTOs);
        accessibilityDTO.getMetadata().addAll(relatedMetadataDTOs);
    }
}