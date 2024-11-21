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

    private AddressDTO mapToDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getLocation(),
                address.getAttention(),
                address.getAddress_1(),
                address.getAddress_2(),
                address.getCity(),
                address.getRegion(),
                address.getStateProvince(),
                address.getPostalCode(),
                address.getCountry(),
                address.getAddressType()
        );
    }

    private Address mapToEntity(AddressDTO addressDTO) {
        return new Address(
                addressDTO.getId(),
                addressDTO.getLocation(),
                addressDTO.getAttention(),
                addressDTO.getAddress_1(),
                addressDTO.getAddress_2(),
                addressDTO.getCity(),
                addressDTO.getRegion(),
                addressDTO.getStateProvince(),
                addressDTO.getPostalCode(),
                addressDTO.getCountry(),
                addressDTO.getAddressType()
        );
    }

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return this.addressRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(String id) {
        Address address = this.addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return this.mapToDTO(address);
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = this.mapToEntity(addressDTO);
        Address savedAddress = this.addressRepository.save(address);
        return this.mapToDTO(savedAddress);
    }

    @Override
    public AddressDTO updateAddress(String id, AddressDTO addressDTO) {
        Address oldAddress = this.addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Address not found"));

        oldAddress.setLocation(addressDTO.getLocation());
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
        return this.mapToDTO(updatedAddress);
    }

    @Override
    public void deleteAddress(String id) {
        Address target = this.addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Address not found"));
        this.addressRepository.delete(target);
    }
}
