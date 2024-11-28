package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(String id);

    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(String id, AddressDTO addressDTO);

    void deleteAddress(String id);
}
