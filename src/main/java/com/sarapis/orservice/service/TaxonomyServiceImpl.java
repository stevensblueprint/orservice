package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.TaxonomyMapper;
import com.sarapis.orservice.model.Taxonomy;
import com.sarapis.orservice.repository.TaxonomyRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxonomyServiceImpl implements  TaxonomyService {
  private final TaxonomyRepository taxonomyRepository;
  private final TaxonomyMapper taxonomyMapper;
  private final MetadataServiceImpl metadataService;
  private final String resourceType = "TaxonomyService";

  @Override
  @Transactional(readOnly = true)
  public List<TaxonomyDTO.Response> getAllTaxonomies() {
    return taxonomyRepository.findAll().stream()
        .map(taxonomyMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public TaxonomyDTO.Response getTaxonomyById(String id) {
    return taxonomyRepository.findById(id)
       .map(taxonomyMapper::toResponseDTO).orElseThrow(() -> new RuntimeException("Taxonomy not found with id: " + id));
  }

  @Override
  @Transactional
  public TaxonomyDTO.Response createTaxonomy(TaxonomyDTO.Request requestDto, String updatedBy) {
    if (requestDto.getId() == null) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    Taxonomy taxonomy = taxonomyMapper.toEntity(requestDto);
    taxonomy = taxonomyRepository.save(taxonomy);
    metadataService.createInitialMetadata(requestDto.getId(),
        resourceType, resourceType, "id", updatedBy);

    return taxonomyMapper.toResponseDTO(taxonomyRepository.findById(taxonomy.getId()).orElseThrow());
  }

  @Override
  @Transactional
  public TaxonomyDTO.Response updateTaxonomy(String id, TaxonomyDTO.UpdateRequest updateDto, String updatedBy) {
    Taxonomy taxonomy = taxonomyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found with id: " + id));
    if (updateDto.getName() != null && !updateDto.getName().equals(taxonomy.getName())) {
      metadataService.createMetadata(id,
          resourceType, "name", taxonomy.getName(), updateDto.getName(), updatedBy);
      taxonomy.setName(updateDto.getName());
    }

    if (updateDto.getDescription() != null && updateDto.getDescription().equals(taxonomy.getDescription())) {
      metadataService.createMetadata(id, resourceType, "description", taxonomy.getDescription(),
          updateDto.getDescription(), updatedBy);
      taxonomy.setDescription(updateDto.getDescription());
    }

    if (updateDto.getVersion() != null && updateDto.getVersion().equals(taxonomy.getVersion())) {
      metadataService.createMetadata(id, resourceType, "version", taxonomy.getVersion(),
          updateDto.getVersion(), updatedBy);
      taxonomy.setVersion(updateDto.getVersion());
    }
    return taxonomyMapper.toResponseDTO(taxonomyRepository.findById(id).orElseThrow());
  }

  @Override
  @Transactional
  public void deleteTaxonomy(String id, String deletedBy) {
    Taxonomy taxonomy = taxonomyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Taxonomy not found with id: " + id));
    metadataService.createMetadata(id, resourceType, taxonomy.getName(),
        taxonomy.getName(),"DELETED", deletedBy);
    taxonomyRepository.deleteById(id);
  }
}
