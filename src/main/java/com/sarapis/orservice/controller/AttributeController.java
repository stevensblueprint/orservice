package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {
    private final AttributeService attributeService;

    @Autowired
    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAllAttributes() {
        List<AttributeDTO> attributes = this.attributeService.getAllAttributes();
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/{attributeId}")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable String attributeId) {
        AttributeDTO attribute = this.attributeService.getAttributeById(attributeId);
        return ResponseEntity.ok(attribute);
    }

    @PostMapping
    public ResponseEntity<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO createdAttribute = this.attributeService.createAttribute(attributeDTO.getLinkId(), attributeDTO);
        return ResponseEntity.ok(createdAttribute);
    }

    @PutMapping("/{attributeId}")
    public ResponseEntity<AttributeDTO> updateAttribute(@PathVariable String attributeId,
                                                        @RequestBody AttributeDTO attributeDTO) {
        AttributeDTO updatedAttribute = this.attributeService.updateAttribute(attributeId, attributeDTO);
        return ResponseEntity.ok(updatedAttribute);
    }

    @DeleteMapping("/{attributeId}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable String attributeId) {
        this.attributeService.deleteAttribute(attributeId);
        return ResponseEntity.noContent().build();
    }
}
