package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    public final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressDTOs = this.addressService.getAllAddresses();
        return ResponseEntity.ok(addressDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable String id) {
        AddressDTO queried = this.addressService.getAddressById(id);
        return ResponseEntity.ok(queried);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = this.addressService.createAddress(addressDTO);
        return ResponseEntity.ok(createdAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable String id, @RequestBody AddressDTO addressDTO) {
        AddressDTO updateAddress = this.addressService.updateAddress(id, addressDTO);
        return ResponseEntity.ok(updateAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        this.addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
