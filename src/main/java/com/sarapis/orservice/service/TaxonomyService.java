package com.sarapis.orservice.service;

import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.mapper.TaxonomyMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.Taxonomy;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.TaxonomyRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxonomyService {
  private final TaxonomyRepository taxonomyRepository;
  private final MetadataRepository metadataRepository;
  private final TaxonomyMapper taxonomyMapper;

  @Transactional(readOnly = true)
  public List<TaxonomyDTO.Response> getAllTaxonomies() {
    return taxonomyRepository.findAll().stream()
        .map(taxonomyMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public TaxonomyDTO.Response getTaxonomyById(String id) {
    return taxonomyRepository.findById(id)
       .map(taxonomyMapper::toResponseDTO).orElseThrow(() -> new RuntimeException("Taxonomy not found with id: " + id));
  }

  @Transactional
  public TaxonomyDTO.Response createTaxonomy(TaxonomyDTO.Request requestDto) {
    if (requestDto.getId() == null) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Taxonomy taxonomy = taxonomyMapper.toEntity(requestDto);
    taxonomy = taxonomyRepository.save(taxonomy);
    createInitialMetadata(requestDto.getId(), taxonomy.getName());

    return taxonomyMapper.toResponseDTO(taxonomyRepository.findById(taxonomy.getId()).orElseThrow());
  }

  @Transactional
  public TaxonomyDTO.Response updateTaxonomy(String id, TaxonomyDTO.UpdateRequest updateDto, String updatedBy) {
    Taxonomy taxonomy = taxonomyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found with id: " + id));
    if (updateDto.getName() != null && !updateDto.getName().equals(taxonomy.getName())) {
      createMetadata(id, "name", taxonomy.getName(), updateDto.getName(), updatedBy);
      taxonomy.setName(updateDto.getName());
    }

    if (updateDto.getDescription() != null && updateDto.getDescription().equals(taxonomy.getDescription())) {
      createMetadata(id, "description", taxonomy.getDescription(), updateDto.getDescription(), updatedBy);
    }

    if (updateDto.getVersion() != null && updateDto.getVersion().equals(taxonomy.getVersion())) {
      createMetadata(id, "version", taxonomy.getVersion(), updateDto.getVersion(), updatedBy);
    }
    return taxonomyMapper.toResponseDTO(taxonomyRepository.findById(id).orElseThrow());
  }

  @Transactional
  public void deleteTaxonomy(String id, String deletedBy) {
    Taxonomy taxonomy = taxonomyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found with id: " + id));
    createMetadata(id, "taxonomy", taxonomy.getName(), "DELETED", deletedBy);
    taxonomyRepository.deleteById(id);
  }

  private void createInitialMetadata(String resourceId, String value) {
    Metadata metadata = new Metadata();
    metadata.setId(UUID.randomUUID().toString());
    metadata.setResourceId(resourceId);
    metadata.setResourceType("taxonomy");
    metadata.setLastActionDate(LocalDate.now());
    metadata.setLastActionType("create");
    metadata.setFieldName("taxonomy");
    metadata.setPreviousValue("");
    metadata.setReplacementValue(value);
    metadata.setUpdatedBy("creation");
    metadataRepository.save(metadata);
  }

  private void createMetadata(String resourceId, String fieldName, String previousValue,
  String replacementValue, String updatedBy) {
    Metadata metadata = new Metadata();
    metadata.setId(UUID.randomUUID().toString());
    metadata.setResourceId(resourceId);
    metadata.setResourceType("taxonomy");
    metadata.setLastActionDate(LocalDate.now());
    metadata.setLastActionType("update");
    metadata.setFieldName(fieldName);
    metadata.setPreviousValue(previousValue);
    metadata.setReplacementValue(replacementValue);
    metadata.setUpdatedBy(updatedBy);

    metadataRepository.save(metadata);

  }
}
