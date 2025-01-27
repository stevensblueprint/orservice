package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final AttributeService attributeService;
    private final MetadataService metadataService;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository,
                            AttributeService attributeService,
                            MetadataService metadataService) {
        this.phoneRepository = phoneRepository;
        this.attributeService = attributeService;
        this.metadataService = metadataService;
    }

    @Override
    public List<PhoneDTO> getAllPhones() {
        List<PhoneDTO> phoneDTOs = this.phoneRepository.findAll()
                .stream().map(Phone::toDTO).toList();
        phoneDTOs.forEach(this::addRelatedData);
        return phoneDTOs;
    }

    @Override
    public PhoneDTO getPhoneById(String phoneId) {
        Phone phone = this.phoneRepository.findById(phoneId)
                .orElseThrow(() -> new RuntimeException("Phone not found."));
        PhoneDTO phoneDTO = phone.toDTO();
        this.addRelatedData(phoneDTO);
        return phoneDTO;
    }

    @Override
    public PhoneDTO createPhone(PhoneDTO phoneDTO) {
        Phone phone = this.phoneRepository.save(phoneDTO.toEntity(null, null, null, null, null));
        phoneDTO.getAttributes()
                .forEach(attributeDTO -> this.attributeService.createAttribute(phone.getId(), attributeDTO));
        phoneDTO.getMetadata().forEach(e -> this.metadataService.createMetadata(phone.getId(), e));

        Phone createdPhone = this.phoneRepository.save(phone);
        return this.getPhoneById(createdPhone.getId());
    }

    @Override
    public PhoneDTO updatePhone(String phoneId, PhoneDTO phoneDTO) {
        Phone oldPhone = this.phoneRepository.findById(phoneId).orElseThrow(() -> new RuntimeException("Phone not found."));

        oldPhone.setNumber(phoneDTO.getNumber());
        oldPhone.setExtension(phoneDTO.getExtension());
        oldPhone.setType(phoneDTO.getType());
        oldPhone.setDescription(phoneDTO.getDescription());

        Phone updatedPhone = this.phoneRepository.save(oldPhone);
        return this.getPhoneById(updatedPhone.getId());
    }

    @Override
    public void deletePhone(String phoneId) {
        Phone phone = this.phoneRepository.findById(phoneId).orElseThrow(() -> new RuntimeException("Phone not found."));
        this.attributeService.deleteRelatedAttributes(phone.getId());
        this.metadataService.deleteRelatedMetadata(phone.getId());
        this.phoneRepository.delete(phone);
    }

    private void addRelatedData(PhoneDTO phoneDTO) {
        phoneDTO.getAttributes().addAll(this.attributeService.getRelatedAttributes(phoneDTO.getId()));
        phoneDTO.getMetadata().addAll(this.metadataService.getRelatedMetadata(phoneDTO.getId()));
    }
}
