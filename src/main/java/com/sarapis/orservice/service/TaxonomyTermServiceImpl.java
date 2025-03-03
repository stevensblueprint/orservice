package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.TaxonomyTermMapper;
import com.sarapis.orservice.model.TaxonomyTerm;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

@Service
@RequiredArgsConstructor
public class TaxonomyTermServiceImpl implements TaxonomyTermService {
  private final TaxonomyTermRepository taxonomyTermRepository;
  private final TaxonomyTermMapper taxonomyTermMapper;
  private final MetadataServiceImpl metadataService;
  private final String resourceType = "TaxonomyTerm";

  @Override
  @Transactional(readOnly = true)
  public List<TaxonomyTermDTO.Response> getAllTaxonomyTerms() {
    return taxonomyTermRepository.findAll().stream()
        .map(taxonomyTermMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
    public TaxonomyTermDTO.Response getTaxonomyTermById(String id) {
    return taxonomyTermRepository.findById(id)
        .map(taxonomyTermMapper::toResponseDTO).orElseThrow(
            () -> new RuntimeException("TaxonomyTerm not found with id: " + id)
        );
  }

  @Override
  @Transactional
  public TaxonomyTermDTO.Response createTaxonomyTerm(TaxonomyTermDTO.Request requestDto, String updatedBy) {
    if (requestDto.getId() == null) {
      requestDto.setId(UUID.randomUUID().toString());
    }

    TaxonomyTerm taxonomyTerm = taxonomyTermMapper.toEntity(requestDto);
    taxonomyTerm = taxonomyTermRepository.save(taxonomyTerm);

    metadataService.createInitialMetadata(taxonomyTerm.getId(), resourceType,
        "taxonomyTerm", taxonomyTerm.getName(), updatedBy);

    return taxonomyTermMapper.toResponseDTO(taxonomyTermRepository.findById(taxonomyTerm.getId()).orElseThrow());
  }

  @Override
  @Transactional
  public TaxonomyTermDTO.Response updateTaxonomyTerm(String id, TaxonomyTermDTO.UpdateRequest updateDto, String updatedBy) {
    TaxonomyTerm taxonomyTerm = taxonomyTermRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Taxonomy Term not found with id: " + id));
    if (updateDto.getCode() != null && !updateDto.getCode().equals(taxonomyTerm.getCode())) {
      metadataService.createMetadata(id, resourceType, "code", taxonomyTerm.getCode(), updateDto.getCode(), updatedBy);
      taxonomyTerm.setCode(updateDto.getCode());
    }

    if (updateDto.getName() != null && ! updateDto.getName().equals(taxonomyTerm.getName())) {
      metadataService.createMetadata(id, resourceType, "name", taxonomyTerm.getName(), updateDto.getName(), updatedBy);
      taxonomyTerm.setName(updateDto.getName());
    }

    if (updateDto.getDescription() != null && !updateDto.getDescription().equals(taxonomyTerm.getDescription())) {
      metadataService.createMetadata(id, resourceType, "description", updateDto.getDescription(), updateDto.getDescription(), updatedBy);
      taxonomyTerm.setDescription(updateDto.getDescription());
    }

    if (updateDto.getLanguage() != null && !updateDto.getLanguage().equals(taxonomyTerm.getLanguage())) {
      metadataService.createMetadata(id, resourceType, "language", updateDto.getLanguage(), taxonomyTerm.getLanguage(), updatedBy);
      taxonomyTerm.setLanguage(updateDto.getLanguage());
    }

    if (updateDto.getTermUri() != null && !updateDto.getTermUri().equals(taxonomyTerm.getTermUri())) {
      metadataService.createMetadata(id, resourceType, "term", updateDto.getTermUri(), taxonomyTerm.getTermUri(), updatedBy);
      taxonomyTerm.setTermUri(updateDto.getTermUri());
    }
    taxonomyTerm = taxonomyTermRepository.save(taxonomyTerm);
    return taxonomyTermMapper.toResponseDTO(taxonomyTermRepository.findById(taxonomyTerm.getId()).orElseThrow());
  }

  @Override
  @Transactional
  public void deleteTaxonomyTerm(String id, String deletedBy) {
    TaxonomyTerm taxonomyTerm = taxonomyTermRepository.findById(id)
       .orElseThrow(() -> new ResourceNotFoundException("Taxonomy Term not found with id: " + id));
    metadataService.createMetadata(id, resourceType, "taxonomyTerm", taxonomyTerm.getName(), "DELETED", deletedBy);
    taxonomyTermRepository.deleteById(id);
  }

}
