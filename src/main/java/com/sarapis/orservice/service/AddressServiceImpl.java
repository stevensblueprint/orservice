package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.entity.Address;
import com.sarapis.orservice.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return this.addressRepository.findAll().stream()
                .map(Address::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(String id) {
        Address address = this.addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return address.toDTO();
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = addressDTO.toEntity();
        Address savedAddress = this.addressRepository.save(address);
        return savedAddress.toDTO();
    }

    @Override
    public AddressDTO updateAddress(String id, AddressDTO addressDTO) {
        Address oldAddress = this.addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        oldAddress.setAttention(addressDTO.getAttention());
        oldAddress.setAddress_1(addressDTO.getAddress_1());
        oldAddress.setAddress_2(addressDTO.getAddress_2());
        oldAddress.setCity(addressDTO.getCity());
        oldAddress.setRegion(addressDTO.getRegion());
        oldAddress.setStateProvince(addressDTO.getStateProvince());
        oldAddress.setPostalCode(addressDTO.getPostalCode());
        oldAddress.setCountry(addressDTO.getCountry());
        oldAddress.setAddressType(addressDTO.getAddressType());

        Address updatedAddress = this.addressRepository.save(oldAddress);
        return updatedAddress.toDTO();
    }

    @Override
    public void deleteAddress(String id) {
        Address target = this.addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Address not found"));
        this.addressRepository.delete(target);
    }
}
