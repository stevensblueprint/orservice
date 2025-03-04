package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.CONTACT_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.ContactDTO.Request;
import com.sarapis.orservice.dto.ContactDTO.Response;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.PhoneDTO;
import com.sarapis.orservice.mapper.ContactMapper;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.repository.ContactRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

  private final ContactMapper contactMapper;
  private final ContactRepository contactRepository;
  private final PhoneService phoneService;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createContact(Request contactDto) {
    if (contactDto.getId() == null || contactDto.getId().trim().isEmpty()) {
      contactDto.setId(UUID.randomUUID().toString());
    }
    Contact contact = contactMapper.toEntity(contactDto);
    Contact savedContact = contactRepository.save(contact);
    MetadataUtils.createMetadataEntry(
        metadataService,
        savedContact.getId(),
        CONTACT_RESOURCE_TYPE,
        CREATE.name(),
        "contact",
        EMPTY_PREVIOUS_VALUE,
        contactDto.getName(),
        "SYSTEM"
    );

    List<PhoneDTO.Response> savedPhones = new ArrayList<>();
    if (contactDto.getPhones() != null) {
      for (PhoneDTO.Request phoneDTO : contactDto.getPhones()) {
        phoneDTO.setContactId(savedContact.getId());
        PhoneDTO.Response savedPhone = phoneService.createPhone(phoneDTO);
        savedPhones.add(savedPhone);
      }
    }

    List<MetadataDTO.Response> metadata =
        metadataService.getMetadataByResourceIdAndResourceType(savedContact.getId(), CONTACT_RESOURCE_TYPE);
    Response response = contactMapper.toResponseDTO(savedContact);
    response.setPhones(savedPhones);
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getContactsByOrganizationId(String organizationId) {
    List<Contact> contacts = contactRepository.findByOrganizationId(organizationId);
    List<ContactDTO.Response> contactDtos = contacts.stream().map(contactMapper::toResponseDTO).toList();
    contactDtos = contactDtos.stream().peek(contact -> {
      List<PhoneDTO.Response> phoneDtos = phoneService.getPhonesByContactId(contact.getId());
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          contact.getId(), CONTACT_RESOURCE_TYPE
      );
      contact.setPhones(phoneDtos);
      contact.setMetadata(metadata);
    }).toList();
    return contactDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getContactsByLocationId(String locationId) {
    List<Contact> contacts = contactRepository.findByLocationId(locationId);
    List<ContactDTO.Response> contactDtos = contacts.stream().map(contactMapper::toResponseDTO).toList();
    contactDtos = contactDtos.stream().peek(contact -> {
      List<PhoneDTO.Response> phoneDtos = phoneService.getPhonesByContactId(contact.getId());
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          contact.getId(), CONTACT_RESOURCE_TYPE
      );
      contact.setPhones(phoneDtos);
      contact.setMetadata(metadata);
    }).toList();
    return contactDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getContactsByServiceId(String serviceId) {
    List<Contact> contacts = contactRepository.findByServiceId(serviceId);
    List<ContactDTO.Response> contactDtos = contacts.stream().map(contactMapper::toResponseDTO).toList();
    contactDtos = contactDtos.stream().peek(contact -> {
      List<PhoneDTO.Response> phoneDtos = phoneService.getPhonesByContactId(contact.getId());
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          contact.getId(), CONTACT_RESOURCE_TYPE
      );
      contact.setPhones(phoneDtos);
      contact.setMetadata(metadata);
    }).toList();
    return contactDtos;
  }
}
