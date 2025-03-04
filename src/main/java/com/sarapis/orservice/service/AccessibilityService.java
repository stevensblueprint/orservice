package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AccessibilityDTO;
import java.util.List;

public interface AccessibilityService {
  AccessibilityDTO.Response createAccessibility(AccessibilityDTO.Request dto);
  List<AccessibilityDTO.Response> getAccessibilityByLocationId(String locationId);
}
