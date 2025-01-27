package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AccessibilityDTO;

import java.util.List;

public interface AccessibilityService {
    List<AccessibilityDTO> getAllAccessibilities();

    AccessibilityDTO getAccessibilityById(String accessibilityId);

    AccessibilityDTO createAccessibility(AccessibilityDTO accessibilityDTO);

    AccessibilityDTO updateAccessibility(String accessibilityId, AccessibilityDTO accessibilityDTO);

    void deleteAccessibility(String accessibilityId);
}