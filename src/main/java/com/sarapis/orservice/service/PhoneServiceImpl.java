package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.AttributeDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.entity.Attribute;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.entity.Metadata;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final AttributeRepository attributeRepository;
    private final MetadataRepository metadataRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository, AttributeRepository attributeRepository, MetadataRepository metadataRepository) {
        this.phoneRepository = phoneRepository;
        this.attributeRepository = attributeRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public List<PhoneDTO> getAllPhones() {
        List<PhoneDTO> phoneDTOs = this.phoneRepository.findAll().stream().map(Phone::toDTO).toList();
        phoneDTOs.forEach(this::addRelatedData);
        return phoneDTOs;
    }

    @Override
    public PhoneDTO getPhoneById(String id) {
        Phone phone = this.phoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phone not found."));
        PhoneDTO phoneDTO = phone.toDTO();
        this.addRelatedData(phoneDTO);
        return phoneDTO;
    }

    @Override
    public PhoneDTO createPhone(PhoneDTO phoneDTO) {
        Phone phone = this.phoneRepository.save(phoneDTO.toEntity());

        for (AttributeDTO attributeDTO : phoneDTO.getAttributes()) {
            this.attributeRepository.save(attributeDTO.toEntity(phone.getId()));
        }

        for (MetadataDTO metadataDTO : phoneDTO.getMetadata()) {
            this.metadataRepository.save(metadataDTO.toEntity(phone.getId()));
        }

        PhoneDTO savedPhoneDTO = this.phoneRepository.save(phone).toDTO();
        this.addRelatedData(savedPhoneDTO);
        return savedPhoneDTO;
    }

    @Override
    public PhoneDTO updatePhone(String id, PhoneDTO phoneDTO) {
        Phone oldPhone = this.phoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phone not found."));

        oldPhone.setNumber(phoneDTO.getNumber());
        oldPhone.setExtension(phoneDTO.getExtension());
        oldPhone.setType(phoneDTO.getType());
        oldPhone.setDescription(phoneDTO.getDescription());

        Phone updatedPhoneDTO = this.phoneRepository.save(oldPhone);
        return updatedPhoneDTO.toDTO();
    }

    @Override
    public void deletePhone(String id) {
        Phone phone = this.phoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phone not found."));
        this.phoneRepository.deleteAttributes(phone.getId());
        this.phoneRepository.deleteMetadata(phone.getId());
        this.phoneRepository.delete(phone);
    }

    private void addRelatedData(PhoneDTO phoneDTO) {
        phoneDTO.getAttributes().addAll(this.phoneRepository.getAttributes(phoneDTO.getId()).stream().map(Attribute::toDTO).toList());
        phoneDTO.getMetadata().addAll(this.phoneRepository.getMetadata(phoneDTO.getId()).stream().map(Metadata::toDTO).toList());
    }
}
