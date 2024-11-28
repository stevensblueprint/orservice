package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attribute")
public class AttributeController {
    private final AttributeService attributeService;

    @Autowired
    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAllAttributes() {
        List<AttributeDTO> attributeDTOs = this.attributeService.getAllAttributes();
        return ResponseEntity.ok(attributeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable String id) {
        AttributeDTO queried = this.attributeService.getAttributeById(id);
        return ResponseEntity.ok(queried);
    }

    @PostMapping
    public ResponseEntity<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO createdAttribute = this.attributeService.createAttribute(attributeDTO);
        return ResponseEntity.ok(createdAttribute);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttributeDTO> updateAttribute(@PathVariable String id, @RequestBody AttributeDTO attributeDTO) {
        AttributeDTO updatedAttribute = this.attributeService.updateAttribute(id, attributeDTO);
        return ResponseEntity.ok(updatedAttribute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable String id) {
        this.attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }
}
