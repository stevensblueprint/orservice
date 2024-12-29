package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO;

import java.util.List;

public interface PhoneService {
    List<PhoneDTO> getAllPhones();

    PhoneDTO getPhoneById(String id);

    PhoneDTO createPhone(PhoneDTO phoneDTO);

    PhoneDTO updatePhone(String id, PhoneDTO phoneDTO);

    void deletePhone(String id);
}
