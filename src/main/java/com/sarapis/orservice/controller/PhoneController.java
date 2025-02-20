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

    @GetMapping("/{id}")
    public ResponseEntity<PhoneDTO> getPhoneById(@PathVariable String id) {
        PhoneDTO phoneDTO = phoneService.getPhoneById(id);
        return ResponseEntity.ok(phoneDTO);
    }

    @PostMapping
    public ResponseEntity<PhoneDTO> createPhone(@RequestBody PhoneDTO phoneDTO) {
        PhoneDTO createdPhone = phoneService.createPhone(phoneDTO);
        return ResponseEntity.ok(createdPhone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhoneDTO> updatePhone(@PathVariable String id, @RequestBody PhoneDTO phoneDTO) {
        PhoneDTO updatedPhone = phoneService.updatePhone(id, phoneDTO);
        return ResponseEntity.ok(updatedPhone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable String id) {
        phoneService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
