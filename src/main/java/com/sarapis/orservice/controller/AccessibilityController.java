package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.service.AccessibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accessibilities")
public class AccessibilityController {
    private final AccessibilityService accessibilityService;

    @Autowired
    public AccessibilityController(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }

    @GetMapping
    public ResponseEntity<List<AccessibilityDTO>> getAllAccessibilities() {
        List<AccessibilityDTO> accessibilities = this.accessibilityService.getAllAccessibilities();
        return ResponseEntity.ok(accessibilities);
    }

    @GetMapping("/{accessibilityId}")
    public ResponseEntity<AccessibilityDTO> getAccessibilityById(@PathVariable String accessibilityId) {
        AccessibilityDTO accessibility = this.accessibilityService.getAccessibilityById(accessibilityId);
        return ResponseEntity.ok(accessibility);
    }

    @PostMapping
    public ResponseEntity<AccessibilityDTO> createAccessibility(@RequestBody AccessibilityDTO accessibilityDTO) {
        AccessibilityDTO createdAccessibility = this.accessibilityService.createAccessibility(accessibilityDTO);
        return ResponseEntity.ok(createdAccessibility);
    }

    @PutMapping("/{accessibilityId}")
    public ResponseEntity<AccessibilityDTO> updateAccessibility(@PathVariable String accessibilityId,
                                                                @RequestBody AccessibilityDTO accessibilityDTO) {
        AccessibilityDTO updatedAccessibility = this.accessibilityService
                .updateAccessibility(accessibilityId, accessibilityDTO);
        return ResponseEntity.ok(updatedAccessibility);
    }

    @DeleteMapping("/{accessibilityId}")
    public ResponseEntity<Void> deleteAccessibility(@PathVariable String accessibilityId) {
        this.accessibilityService.deleteAccessibility(accessibilityId);
        return ResponseEntity.noContent().build();
    }
}