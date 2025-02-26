package com.sarapis.orservice.service;

import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.sarapis.orservice.dto.UrlDTO;
import com.sarapis.orservice.mapper.AttributeMapper;
import com.sarapis.orservice.mapper.UrlMapper;
import com.sarapis.orservice.model.Attribute;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Url;
import com.sarapis.orservice.repository.AttributeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.UrlRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlService {
  private final UrlRepository urlRepository;
  private final AttributeRepository attributeRepository;
  private final MetadataRepository metadataRepository;
  private final UrlMapper urlMapper;
  private final AttributeMapper attributeMapper;

  @Transactional(readOnly = true)
  public List<UrlDTO.Response> getAllUrls() {
    return urlRepository.findAll().stream()
        .map(urlMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public UrlDTO.Response getUrlById(String id) {
    return urlRepository.findById(id)
       .map(urlMapper::toResponseDTO).orElseThrow(() -> new ResourceNotFoundException("URL not found with id: " + id));
  }

  @Transactional
  public UrlDTO.Response createUrl(UrlDTO.Request requestDto) {
    if (requestDto.getId() == null) {
      requestDto.setId(UUID.randomUUID().toString());
    }

    Url url = urlMapper.toEntity(requestDto);
    url = urlRepository.save(url);

    // Handle attributes if present
    if (requestDto.getAttributes() != null && !requestDto.getAttributes().isEmpty()) {
      Url finalUrl = url;
      List<Attribute> attributes = requestDto.getAttributes().stream()
          .map(attrDto -> {
            Attribute attr = attributeMapper.toEntity(attrDto);
            attr.setLinkId(finalUrl.getId());
            attr.setLinkEntity("url");
            return attr;
          })
          .collect(Collectors.toList());
      attributeRepository.saveAll(attributes);
    }

    // Create initial metadata
    createInitialMetadata(url.getId(), url.getUrl());

    return urlMapper.toResponseDTO(urlRepository.findById(url.getId()).orElseThrow());
  }

  @Transactional
  public UrlDTO.Response updateUrl(String id, UrlDTO.UpdateRequest updateDto, String updatedBy) {
    Url url = urlRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("URL not found with id: " + id));

    // Track changes for metadata
    if (updateDto.getLabel() != null && !updateDto.getLabel().equals(url.getLabel())) {
      createMetadata(id, "label", url.getLabel(), updateDto.getLabel(), updatedBy);
      url.setLabel(updateDto.getLabel());
    }

    if (updateDto.getUrl() != null && !updateDto.getUrl().equals(url.getUrl())) {
      createMetadata(id, "url", url.getUrl(), updateDto.getUrl(), updatedBy);
      url.setUrl(updateDto.getUrl());
    }

    if (updateDto.getOrganizationId() != null && !updateDto.getOrganizationId().equals(url.getOrganizationId())) {
      createMetadata(id, "organization_id",
          url.getOrganizationId() != null ? url.getOrganizationId() : "",
          updateDto.getOrganizationId(), updatedBy);
      url.setOrganizationId(updateDto.getOrganizationId());
    }

    if (updateDto.getServiceId() != null && !updateDto.getServiceId().equals(url.getServiceId())) {
      createMetadata(id, "service_id",
          url.getServiceId() != null ? url.getServiceId() : "",
          updateDto.getServiceId(), updatedBy);
      url.setServiceId(updateDto.getServiceId());
    }

    url = urlRepository.save(url);

    // Handle attributes updates if provided
    if (updateDto.getAttributes() != null) {
      attributeRepository.deleteByLinkIdAndLinkEntity(id, "url");
      if (!updateDto.getAttributes().isEmpty()) {
        Url finalUrl = url;
        List<Attribute> attributes = updateDto.getAttributes().stream()
            .map(attrDto -> {
              Attribute attr = attributeMapper.toEntity(attrDto);
              attr.setLinkId(finalUrl.getId());
              attr.setLinkEntity("url");
              return attr;
            })
            .collect(Collectors.toList());
        attributeRepository.saveAll(attributes);

        // Create metadata for attributes update
        createMetadata(id, "attributes", "Previous attributes",
            "Updated attributes", updatedBy);
      }
    }

    return urlMapper.toResponseDTO(urlRepository.findById(url.getId()).orElseThrow());
  }

  @Transactional
  public void deleteUrl(String id, String deletedBy) {
    Url url = urlRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("URL not found with id: " + id));

    // Create deletion metadata
    createMetadata(id, "url", url.getUrl(), "DELETED", deletedBy);

    // Delete associated attributes
    attributeRepository.deleteByLinkIdAndLinkEntity(id, "url");

    // Delete the URL
    urlRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<UrlDTO.Response> getUrlsByOrganizationId(String organizationId) {
    return urlRepository.findByOrganizationId(organizationId).stream()
        .map(urlMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<UrlDTO.Response> getUrlsByServiceId(String serviceId) {
    return urlRepository.findByServiceId(serviceId).stream()
        .map(urlMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  private void createInitialMetadata(String resourceId,
      String value) {
    Metadata metadata = new Metadata();
    metadata.setId(UUID.randomUUID().toString());
    metadata.setResourceId(resourceId);
    metadata.setResourceType("url");
    metadata.setLastActionDate(LocalDate.now());
    metadata.setLastActionType("create");
    metadata.setFieldName("url");
    metadata.setPreviousValue("");
    metadata.setReplacementValue(value);
    metadata.setUpdatedBy("creation");

    metadataRepository.save(metadata);
  }

  private void createMetadata(String resourceId, String fieldName,
      String previousValue, String replacementValue, String updatedBy) {
    Metadata metadata = new Metadata();
    metadata.setId(UUID.randomUUID().toString());
    metadata.setResourceId(resourceId);
    metadata.setResourceType("url");
    metadata.setLastActionDate(LocalDate.now());
    metadata.setLastActionType("update");
    metadata.setFieldName(fieldName);
    metadata.setPreviousValue(previousValue);
    metadata.setReplacementValue(replacementValue);
    metadata.setUpdatedBy(updatedBy);

    metadataRepository.save(metadata);
  }
}
