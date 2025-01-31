package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.AccessibilityDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.AccessibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accessibilities")
public class AccessibilityController {
    private final AccessibilityService accessibilityService;

    @Autowired
    public AccessibilityController(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<AccessibilityDTO>> getAllAccessibilities(@RequestParam(defaultValue = "1") Integer page,
                                                                                 @RequestParam(defaultValue = "10") Integer perPage) {
        List<AccessibilityDTO> accessibilityDTOs = this.accessibilityService.getAllAccessibilities();
        PaginationDTO<AccessibilityDTO> paginationDTO = PaginationDTO.of(
            accessibilityDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessibilityDTO> getAccessibilityById(@PathVariable String id) {
        AccessibilityDTO accessibility = this.accessibilityService.getAccessibilityById(id);
        return ResponseEntity.ok(accessibility);
    }

    @PostMapping
    public ResponseEntity<AccessibilityDTO> createAccessibility(@RequestBody AccessibilityDTO accessibilityDTO) {
        if (accessibilityDTO.getId() == null) {
            accessibilityDTO.setId(UUID.randomUUID().toString());
        }
        AccessibilityDTO createdAccessibility = this.accessibilityService.createAccessibility(accessibilityDTO);
        return ResponseEntity.ok(createdAccessibility);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessibilityDTO> updateAccessibility(@PathVariable String id, @RequestBody AccessibilityDTO accessibilityDTO) {
        AccessibilityDTO updatedAccessibility = this.accessibilityService.updateAccessibility(id, accessibilityDTO);
        return ResponseEntity.ok(updatedAccessibility);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessibility(@PathVariable String id) {
        this.accessibilityService.deleteAccessibility(id);
        return ResponseEntity.noContent().build();
    }
}