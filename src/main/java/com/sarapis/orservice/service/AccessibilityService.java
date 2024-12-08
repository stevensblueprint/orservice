package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AccessibilityDTO;

import java.util.List;

public interface AccessibilityService {
    List<AccessibilityDTO> getAllAccessibilities();

    AccessibilityDTO getAccessibilityById(String id);

    AccessibilityDTO createAccessibility(AccessibilityDTO accessibilityDTO);

    AccessibilityDTO updateAccessibility(String id, AccessibilityDTO accessibilityDTO);

    void deleteAccessibility(String id);
}