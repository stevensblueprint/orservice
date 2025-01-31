package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/phones")
public class PhoneController {
    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<PhoneDTO>> getAllPhones(@RequestParam(defaultValue = "1") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer perPage) {
        List<PhoneDTO> phoneDTOs = this.phoneService.getAllPhones();
        PaginationDTO<PhoneDTO> paginationDTO = PaginationDTO.of(
            phoneDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneDTO> getPhoneById(@PathVariable String id) {
        PhoneDTO phoneDTO = this.phoneService.getPhoneById(id);
        return ResponseEntity.ok(phoneDTO);
    }

    @PostMapping
    public ResponseEntity<PhoneDTO> createPhone(@RequestBody PhoneDTO phoneDTO) {
        if (phoneDTO.getId() == null) {
            phoneDTO.setId(UUID.randomUUID().toString());
        }
        PhoneDTO createdPhone = this.phoneService.createPhone(phoneDTO);
        return ResponseEntity.ok(createdPhone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhoneDTO> updatePhone(@PathVariable String id, @RequestBody PhoneDTO phoneDTO) {
        PhoneDTO updatedAccessibility = this.phoneService.updatePhone(id, phoneDTO);
        return ResponseEntity.ok(updatedAccessibility);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable String id) {
        this.phoneService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
