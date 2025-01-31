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
    public ResponseEntity<PaginationDTO<AddressDTO>> getAllAddresses(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                     @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
        List<AddressDTO> addressDTOs = this.addressService.getAllAddresses();

        try {
            PaginationDTO<AddressDTO> paginationDTO = PaginationDTO.of(
                    addressDTOs,
                    page,
                    perPage
            );
            return ResponseEntity.ok(paginationDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable String addressId) {
        AddressDTO address = this.addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = this.addressService.createAddress(addressDTO);
        return ResponseEntity.ok(createdAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable String addressId,
                                                    @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = this.addressService.updateAddress(addressId, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String addressId) {
        this.addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
