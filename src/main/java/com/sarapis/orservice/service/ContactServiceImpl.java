package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO.Request;
import com.sarapis.orservice.dto.ContactDTO.Response;
import com.sarapis.orservice.mapper.ContactMapper;
import com.sarapis.orservice.model.Contact;
import com.sarapis.orservice.repository.ContactRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

  private final ContactMapper contactMapper;
  private final ContactRepository contactRepository;

  @Override
  @Transactional
  public Response createContact(Request contactDto) {
    if (contactDto.getId() == null || contactDto.getId().trim().isEmpty()) {
      contactDto.setId(UUID.randomUUID().toString());
    }
    Contact contact = contactMapper.toEntity(contactDto);
    Contact savedContact = contactRepository.save(contact);
    return contactMapper.toResponseDTO(savedContact);
  }

  @Override
  public List<Response> getContactsByOrganizationId(String organizationId) {
    List<Contact> contacts = contactRepository.findByOrganizationId(organizationId);
    return contacts.stream().map(contactMapper::toResponseDTO).collect(Collectors.toList());
  }
}
