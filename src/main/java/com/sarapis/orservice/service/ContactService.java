package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ContactDTO;
import java.util.List;

public interface ContactService {
  ContactDTO.Response createContact(ContactDTO.Request contact);
  List<ContactDTO.Response> getContactsByOrganizationId(String organizationId);
  List<ContactDTO.Response> getContactsByLocationId(String locationId);
  List<ContactDTO.Response> getContactsByServiceId(String serviceId);
  List<ContactDTO.Response> getContactsByServiceAtLocationId(String serviceAtLocationId);
}
