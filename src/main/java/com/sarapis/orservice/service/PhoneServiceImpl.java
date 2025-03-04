package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.PHONE_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.dto.PhoneDTO.Request;
import com.sarapis.orservice.dto.PhoneDTO.Response;
import com.sarapis.orservice.mapper.PhoneMapper;
import com.sarapis.orservice.model.Phone;
import com.sarapis.orservice.repository.PhoneRepository;
import com.sarapis.orservice.utils.MetadataUtils;
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
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedPhone.getId(), PHONE_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  public List<Response> getPhonesByOrganizationId(String organizationId) {
    List<Phone> phones = phoneRepository.findByOrganizationId(organizationId);
    List<PhoneDTO.Response> phoneDtos = phones.stream().map(phoneMapper::toResponseDTO).toList();
    phoneDtos = phoneDtos.stream().peek(phone -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          phone.getId(), PHONE_RESOURCE_TYPE
      );
      phone.setMetadata(metadata);
    }).toList();
    return phoneDtos;
  }
}
