package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    public final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<AddressDTO>> getAllAddresses(@RequestParam(defaultValue = "1") Integer page,
                                                                     @RequestParam(defaultValue = "10") Integer perPage) {
        List<AddressDTO> addressDTOs = this.addressService.getAllAddresses();
        PaginationDTO<AddressDTO> paginationDTO = PaginationDTO.of(
            addressDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
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
