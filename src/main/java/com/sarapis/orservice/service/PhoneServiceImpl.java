package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PhoneDTO.Request;
import com.sarapis.orservice.dto.PhoneDTO.Response;
import com.sarapis.orservice.mapper.PhoneMapper;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.repository.PhoneRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

  private final PhoneMapper phoneMapper;
  private final PhoneRepository phoneRepository;

  @Override
  @Transactional
  public Response createPhone(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Phone phone = phoneMapper.toEntity(dto);
    Phone savedPhone = phoneRepository.save(phone);
    return phoneMapper.toResponseDTO(savedPhone);
  }

  @Override
  public List<Response> getPhonesByOrganizationId(String organizationId) {
    List<Phone> phones = phoneRepository.findByOrganizationId(organizationId);
    return phones.stream().map(phoneMapper::toResponseDTO).collect(Collectors.toList());
  }
}
