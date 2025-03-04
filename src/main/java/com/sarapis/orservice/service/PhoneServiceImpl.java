package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.PHONE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.LanguageDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.PhoneDTO.Request;
import com.sarapis.orservice.dto.PhoneDTO.Response;
import com.sarapis.orservice.mapper.PhoneMapper;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

  private final PhoneMapper phoneMapper;
  private final PhoneRepository phoneRepository;
  private final LanguageService languageService;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createPhone(Request dto) {
    if (dto.getId() == null || dto.getId().trim().isEmpty()) {
      dto.setId(UUID.randomUUID().toString());
    }
    Phone phone = phoneMapper.toEntity(dto);
    Phone savedPhone = phoneRepository.save(phone);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedPhone.getId(),
        PHONE_RESOURCE_TYPE,
        "CREATE",
        "phone_number",
        EMPTY_PREVIOUS_VALUE,
        dto.getNumber(),
        "SYSTEM"
    );
    PhoneDTO.Response response = phoneMapper.toResponseDTO(savedPhone);

    List<LanguageDTO.Response> savedLanguages = new ArrayList<>();
    if (dto.getLanguages() != null) {
      for (LanguageDTO.Request languageDTO : dto.getLanguages()) {
        languageDTO.setPhoneId(savedPhone.getId());
        LanguageDTO.Response savedLanguage = languageService.createLanguage(languageDTO);
        savedLanguages.add(savedLanguage);
      }
    }

    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedPhone.getId(), PHONE_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    response.setLanguages(savedLanguages);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getPhonesByOrganizationId(String organizationId) {
    List<Phone> phones = phoneRepository.findByOrganizationId(organizationId);
    List<PhoneDTO.Response> phoneDtos = phones.stream().map(phoneMapper::toResponseDTO).toList();
    phoneDtos = phoneDtos.stream().peek(phone -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          phone.getId(), PHONE_RESOURCE_TYPE
      );
      phone.setLanguages(languageService.getLanguagesByPhoneId(phone.getId()));
      phone.setMetadata(metadata);
    }).toList();
    return phoneDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getPhonesByContactId(String contactId) {
    List<Phone> phones = phoneRepository.findByContactId(contactId);
    List<PhoneDTO.Response> phoneDtos = phones.stream().map(phoneMapper::toResponseDTO).toList();
    phoneDtos = phoneDtos.stream().peek(phone -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          phone.getId(), PHONE_RESOURCE_TYPE
      );
      phone.setLanguages(languageService.getLanguagesByPhoneId(phone.getId()));
      phone.setMetadata(metadata);
    }).toList();
    return phoneDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getPhonesByLocationId(String locationId) {
    List<Phone> phones = phoneRepository.findByLocationId(locationId);
    List<PhoneDTO.Response> phoneDtos = phones.stream().map(phoneMapper::toResponseDTO).toList();
    phoneDtos = phoneDtos.stream().peek(phone -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          phone.getId(), PHONE_RESOURCE_TYPE
      );
      phone.setLanguages(languageService.getLanguagesByPhoneId(phone.getId()));
      phone.setMetadata(metadata);
    }).toList();
    return phoneDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getPhonesByServiceId(String serviceId) {
    List<Phone> phones = phoneRepository.findByServiceId(serviceId);
    List<PhoneDTO.Response> phoneDtos = phones.stream().map(phoneMapper::toResponseDTO).toList();
    phoneDtos = phoneDtos.stream().peek(phone -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          phone.getId(), PHONE_RESOURCE_TYPE
      );
      phone.setLanguages(languageService.getLanguagesByPhoneId(phone.getId()));
      phone.setMetadata(metadata);
    }).toList();
    return phoneDtos;
  }
}
