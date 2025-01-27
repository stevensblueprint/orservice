package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.entity.Address;
import com.sarapis.orservice.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    public AddressServiceImpl(AddressRepository addressRepository,
                              AttributeService attributeService,
                              MetadataService metadataService) {
        this.addressRepository = addressRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<AddressDTO> addressDTOs = this.addressRepository.findAll()
                .stream().map(Address::toDTO).toList();
        addressDTOs.forEach(this::addRelatedData);
        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(String addressId) {
        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found."));
        AddressDTO addressDTO = address.toDTO();
        this.addRelatedData(addressDTO);
        return addressDTO;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = this.addressRepository.save(addressDTO.toEntity(null));
        addressDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(address.getId(), attributeDTO));
        addressDTO.getMetadata()
                .forEach(e -> this.metadataService.createMetadata(address.getId(), e));

        Address createdAddress = this.addressRepository.save(address);
        return this.getAddressById(createdAddress.getId());
    }

    @Override
    public AddressDTO updateAddress(String addressId, AddressDTO addressDTO) {
        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found."));

        address.setAttention(addressDTO.getAttention());
        address.setAddress_1(addressDTO.getAddress_1());
        address.setAddress_2(addressDTO.getAddress_2());
        address.setCity(addressDTO.getCity());
        address.setRegion(addressDTO.getRegion());
        address.setStateProvince(addressDTO.getStateProvince());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        address.setAddressType(addressDTO.getAddressType());

        Address updatedAddress = this.addressRepository.save(address);
        return this.getAddressById(updatedAddress.getId());
    }

    @Override
    public void deleteAddress(String addressId) {
        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found."));
        this.attributeService.deleteRelatedAttributes(address.getId());
        this.metadataService.deleteRelatedMetadata(address.getId());
        this.addressRepository.delete(address);
    }

    private void addRelatedData(AddressDTO addressDTO) {
        addressDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(addressDTO.getId()));
        addressDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(addressDTO.getId()));
    }
}
