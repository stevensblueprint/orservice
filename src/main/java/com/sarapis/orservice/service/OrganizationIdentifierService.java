package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import java.util.List;

public interface OrganizationIdentifierService {
  OrganizationIdentifierDTO.Response createOrganizationIdentifier(OrganizationIdentifierDTO.Request request);
  List<OrganizationIdentifierDTO.Response> getOrganizationIdentifiersByOrganizationId(String organizationId);
}
