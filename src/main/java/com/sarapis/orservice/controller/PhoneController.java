package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.exception.InvalidInputException;
import com.sarapis.orservice.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phones")
public class PhoneController {
    private final PhoneService phoneService;

    @Autowired
    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<PhoneDTO>> getAllPhones(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
        List<PhoneDTO> phoneDTOs = this.phoneService.getAllPhones();

        if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
        if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

        PaginationDTO<PhoneDTO> paginationDTO = PaginationDTO.of(
            phoneDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{phoneId}")
    public ResponseEntity<PhoneDTO> getPhoneById(@PathVariable String phoneId) {
        PhoneDTO phone = this.phoneService.getPhoneById(phoneId);
        return ResponseEntity.ok(phone);
    }

    @PostMapping
    public ResponseEntity<PhoneDTO> createPhone(@RequestBody PhoneDTO phoneDTO) {
        PhoneDTO createdPhone = this.phoneService.createPhone(phoneDTO);
        return ResponseEntity.ok(createdPhone);
    }

    @PutMapping("/{phoneId}")
    public ResponseEntity<PhoneDTO> updatePhone(@PathVariable String phoneId, @RequestBody PhoneDTO phoneDTO) {
        PhoneDTO updatedPhone = this.phoneService.updatePhone(phoneId, phoneDTO);
        return ResponseEntity.ok(updatedPhone);
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> deletePhone(@PathVariable String phoneId) {
        this.phoneService.deletePhone(phoneId);
        return ResponseEntity.noContent().build();
    }
}
