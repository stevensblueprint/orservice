package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.entity.Accessibility;
import com.sarapis.orservice.repository.AccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessibilityServiceImpl implements AccessibilityService {

    private final AccessibilityRepository accessibilityRepository;

    @Autowired
    public AccessibilityServiceImpl(AccessibilityRepository accessibilityRepository) {
        this.accessibilityRepository = accessibilityRepository;
    }

    @Override
    public List<AccessibilityDTO> getAllAccessibilities() {
        return this.accessibilityRepository.findAll()
                .stream()
                .map(Accessibility::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccessibilityDTO getAccessibilityById(String id) {
        Accessibility accessibility = this.accessibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessibility not found"));
        return accessibility.toDTO();
    }

    @Override
    public AccessibilityDTO createAccessibility(AccessibilityDTO accessibilityDTO) {
        Accessibility accessibility = accessibilityDTO.toEntity();
        Accessibility savedAccessibility = this.accessibilityRepository.save(accessibility);
        return savedAccessibility.toDTO();
    }

    @Override
    public AccessibilityDTO updateAccessibility(String id, AccessibilityDTO accessibilityDTO) {
        Accessibility oldAccessibility = this.accessibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessibility not found"));

        oldAccessibility.setDescription(accessibilityDTO.getDescription());
        oldAccessibility.setDetails(accessibilityDTO.getDetails());
        oldAccessibility.setUrl(accessibilityDTO.getUrl());

        Accessibility updatedAccessibility = this.accessibilityRepository.save(oldAccessibility);
        return updatedAccessibility.toDTO();
    }

    @Override
    public void deleteAccessibility(String id) {
        Accessibility target = this.accessibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessibility not found"));
        this.accessibilityRepository.delete(target);
    }
}