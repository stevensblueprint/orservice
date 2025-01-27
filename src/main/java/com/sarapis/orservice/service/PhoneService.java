package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO;

import java.util.List;

public interface PhoneService {
    List<PhoneDTO> getAllPhones();

    PhoneDTO getPhoneById(String phoneId);

    PhoneDTO createPhone(PhoneDTO phoneDTO);

    PhoneDTO updatePhone(String phoneId, PhoneDTO phoneDTO);

    void deletePhone(String phoneId);
}
