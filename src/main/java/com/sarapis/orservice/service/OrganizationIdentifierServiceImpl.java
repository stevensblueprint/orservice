package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.OrganizationIdentifierDTO.Request;
import com.sarapis.orservice.dto.OrganizationIdentifierDTO.Response;
import com.sarapis.orservice.mapper.OrganizationIdentifierMapper;
import com.sarapis.orservice.model.OrganizationIdentifier;
import com.sarapis.orservice.repository.OrganizationIdentifiersRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationIdentifierServiceImpl implements OrganizationIdentifierService{
  private final OrganizationIdentifiersRepository organizationIdentifiersRepository;
  private final OrganizationIdentifierMapper organizationIdentifierMapper;

  @Override
  public Response createOrganizationIdentifier(Request request) {
    if (request.getId() == null || request.getId().trim().isEmpty()) {
      request.setId(UUID.randomUUID().toString());
    }
    OrganizationIdentifier organizationIdentifier = organizationIdentifierMapper.toEntity(request);
    OrganizationIdentifier savedOrganizationIdentifier = organizationIdentifiersRepository.save(organizationIdentifier);
    return organizationIdentifierMapper.toResponseDTO(savedOrganizationIdentifier);
  }

  @Override
  public List<Response> getOrganizationIdentifiersByOrganizationId(String organizationId) {
    List<OrganizationIdentifier> organizationIdentifiers = organizationIdentifiersRepository.findByOrganizationId(organizationId);
    return organizationIdentifiers.stream().map(organizationIdentifierMapper::toResponseDTO).collect(
        Collectors.toList());
  }
}
