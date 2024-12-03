package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Address;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AddressRepository;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    public AddressServiceImpl(AddressRepository addressRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.addressRepository = addressRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<AddressDTO> addressDTOs = this.addressRepository.findAll().stream().map(Address::toDTO).toList();
        addressDTOs.forEach(addressDTO -> {
            addressDTO.getAttributes().addAll(this.addressRepository.getAttributes(addressDTO.getId()).stream().map(Attribute::toDTO).toList());
            addressDTO.getMetadata().addAll(this.addressRepository.getMetadata(addressDTO.getId()).stream().map(Metadata::toDTO).toList());
        });
        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(String id) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found."));
        AddressDTO addressDTO = address.toDTO();
        addressDTO.getAttributes().addAll(this.addressRepository.getAttributes(addressDTO.getId()).stream().map(Attribute::toDTO).toList());
        addressDTO.getMetadata().addAll(this.addressRepository.getMetadata(addressDTO.getId()).stream().map(Metadata::toDTO).toList());
        return addressDTO;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = this.addressRepository.save(addressDTO.toEntity());

        for (AttributeDTO attributeDTO : addressDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(address.getId()));
        }

        for (MetadataDTO metadataDTO : addressDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(address.getId()));
        }

        AddressDTO savedAddressDTO = this.addressRepository.save(address).toDTO();
        savedAddressDTO.getAttributes().addAll(this.addressRepository.getAttributes(savedAddressDTO.getId()).stream().map(Attribute::toDTO).toList());
        savedAddressDTO.getMetadata().addAll(this.addressRepository.getMetadata(savedAddressDTO.getId()).stream().map(Metadata::toDTO).toList());
        return savedAddressDTO;
    }

    @Override
    public AddressDTO updateAddress(String id, AddressDTO addressDTO) {
        Address oldAddress = this.addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found."));

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
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found."));

        this.addressRepository.deleteAttributes(address.getId());
        this.addressRepository.deleteMetadata(address.getId());
        this.addressRepository.delete(address);
    }
}
