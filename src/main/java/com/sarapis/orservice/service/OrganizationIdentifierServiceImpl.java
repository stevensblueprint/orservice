package com.sarapis.orservice.service;

import static com.sarapis.orservice.utils.Metadata.CREATE;
import static com.sarapis.orservice.utils.MetadataUtils.EMPTY_PREVIOUS_VALUE;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE;

import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.OrganizationIdentifierDTO;
import com.sarapis.orservice.dto.OrganizationIdentifierDTO.Request;
import com.sarapis.orservice.dto.OrganizationIdentifierDTO.Response;
import com.sarapis.orservice.mapper.OrganizationIdentifierMapper;
import com.sarapis.orservice.model.OrganizationIdentifier;
import com.sarapis.orservice.repository.OrganizationIdentifiersRepository;
import com.sarapis.orservice.utils.MetadataUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationIdentifierServiceImpl implements OrganizationIdentifierService{
  private final OrganizationIdentifiersRepository organizationIdentifiersRepository;
  private final OrganizationIdentifierMapper organizationIdentifierMapper;
  private final MetadataService metadataService;

  @Override
  @Transactional
  public Response createOrganizationIdentifier(Request request) {
    if (request.getId() == null || request.getId().trim().isEmpty()) {
      request.setId(UUID.randomUUID().toString());
    }
    OrganizationIdentifier organizationIdentifier = organizationIdentifierMapper.toEntity(request);
    OrganizationIdentifier savedOrganizationIdentifier = organizationIdentifiersRepository.save(organizationIdentifier);
    MetadataUtils.createMetadataEntry(
        metadataService,
        request.getId(),
        ORGANIZATION_IDENTIFIER_RESOURCE_TYPE,
        CREATE.name(),
        "organization_identifier",
        EMPTY_PREVIOUS_VALUE,
        request.getOrganizationId(),
        "SYSTEM"
    );
    OrganizationIdentifierDTO.Response response =  organizationIdentifierMapper.toResponseDTO(savedOrganizationIdentifier);
    List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
        request.getId(), ORGANIZATION_IDENTIFIER_RESOURCE_TYPE
    );
    response.setMetadata(metadata);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Response> getOrganizationIdentifiersByOrganizationId(String organizationId) {
    List<OrganizationIdentifier> organizationIdentifiers = organizationIdentifiersRepository.findByOrganizationId(organizationId);
    List<OrganizationIdentifierDTO.Response> responses =  organizationIdentifiers.stream().map(organizationIdentifierMapper::toResponseDTO).toList();
    responses = responses.stream().peek(organizationIdentifier -> {
      List<MetadataDTO.Response> metadata = metadataService.getMetadataByResourceIdAndResourceType(
          organizationIdentifier.getId(), ORGANIZATION_IDENTIFIER_RESOURCE_TYPE
      );
      organizationIdentifier.setMetadata(metadata);
    }).toList();
    return responses;
  }
}
