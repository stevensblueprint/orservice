package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.entity.Phone;
import com.sarapis.orservice.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public List<PhoneDTO> getAllPhones() {
        return phoneRepository.findAll()
            .stream()
            .map(Phone::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public PhoneDTO getPhoneById(String id) {
        Phone phone = phoneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Phone not found with id: " + id));
        return phone.toDTO();
    }

    @Override
    public PhoneDTO createPhone(PhoneDTO phoneDTO) {
        // The Phone entity's ID is generated automatically.
        Phone phone = mapToEntity(phoneDTO);
        Phone savedPhone = phoneRepository.save(phone);
        return savedPhone.toDTO();
    }

    @Override
    public PhoneDTO updatePhone(String id, PhoneDTO phoneDTO) {
        Phone existing = phoneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Phone not found with id: " + id));
        // Update basic fields. For relationships, add logic as needed.
        existing.setNumber(phoneDTO.getNumber());
        existing.setExtension(phoneDTO.getExtension());
        existing.setType(phoneDTO.getType());
        existing.setDescription(phoneDTO.getDescription());
        // Note: Updating the languages collection or related entities may require additional handling.
        Phone updated = phoneRepository.save(existing);
        return updated.toDTO();
    }

    @Override
    public void deletePhone(String id) {
        Phone existing = phoneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Phone not found with id: " + id));
        phoneRepository.delete(existing);
    }

    private Phone mapToEntity(PhoneDTO dto) {
        return Phone.builder()
            // Omit setting the ID because it is generated.
            .number(dto.getNumber())
            .extension(dto.getExtension())
            .type(dto.getType())
            .description(dto.getDescription())
            .build();
    }
}
