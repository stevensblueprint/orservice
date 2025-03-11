package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.ADDRESS_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.DEFAULT_CREATED_BY;

import com.sarapis.orservice.dto.AddressDTO;
import com.sarapis.orservice.dto.AddressDTO.Request;
import com.sarapis.orservice.dto.AddressDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.mapper.AddressMapper;
import com.sarapis.orservice.model.Address;
import com.sarapis.orservice.repository.AddressRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;
  private final MetadataService metadataService;
  @Override
  @Transactional
  public Response createAddress(Request request) {
    if (request.getId() == null || request.getId().trim().isEmpty()) {
      request.setId(UUID.randomUUID().toString());
    }
    Address address = addressMapper.toEntity(request);
    Address savedAddress = addressRepository.save(address);
    metadataService.createMetadata(
        null,
        savedAddress,
        ADDRESS_RESOURCE_TYPE,
        CREATE,
        DEFAULT_CREATED_BY
    );
    AddressDTO.Response response = addressMapper.toResponseDTO(savedAddress);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        savedAddress.getId(), ADDRESS_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getAddressesByLocationId(String locationId) {
    List<Address> addresses = addressRepository.findByLocationId(locationId);
    List<AddressDTO.Response> addressDtos = addresses.stream().map(addressMapper::toResponseDTO).toList();
    addressDtos = addressDtos.stream().peek(address -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          address.getId(), ADDRESS_RESOURCE_TYPE
      );
      address.setMetadata(metadata);
    }).toList();
    return addressDtos;
  }
}
