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
import java.util.Objects;

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
        addressDTOs.forEach(this::getRelatedData);
        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(String id) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found."));
        AddressDTO addressDTO = address.toDTO();
        getRelatedData(addressDTO);
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
        getRelatedData(savedAddressDTO);
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

        List<Attribute> attributes = this.attributeRepository.findAll();
        List<Attribute> relatedAttributes = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), address.getId())).toList();
        this.attributeRepository.deleteAll(relatedAttributes);

        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<Metadata> relatedMetadatas = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), address.getId())).toList();
        this.metadataRepository.deleteAll(relatedMetadatas);

        this.addressRepository.delete(address);
    }

    private void getRelatedData(AddressDTO addressDTO) {
        List<Attribute> attributes = this.attributeRepository.findAll();
        List<AttributeDTO> relatedAttributeDTOs = attributes.stream().filter((e) -> Objects.equals(e.getLinkId(), addressDTO.getId())).map(Attribute::toDTO).toList();

        List<Metadata> metadatas = this.metadataRepository.findAll();
        List<MetadataDTO> relatedMetadataDTOs = metadatas.stream().filter((e) -> Objects.equals(e.getResourceId(), addressDTO.getId())).map(Metadata::toDTO).toList();

        addressDTO.getAttributes().addAll(relatedAttributeDTOs);
        addressDTO.getMetadata().addAll(relatedMetadataDTOs);
    }
}
