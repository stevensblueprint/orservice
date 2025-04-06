package com.sarapis.orservice.service;


import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Request;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.TaxonomyTermMapper;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.model.TaxonomyTerm;
import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import com.sarapis.orservice.repository.TaxonomyTermSpecifications;

import com.sarapis.orservice.utils.MetadataUtils;
import static com.sarapis.orservice.utils.FieldMap.TAXONOMY_TERM_FIELD_MAP;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxonomyTermServiceImpl implements TaxonomyTermService {
  private final TaxonomyTermRepository taxonomyTermRepository;
  private final TaxonomyTermMapper taxonomyTermMapper;
  private final MetadataService metadataService;
  private final MetadataRepository metadataRepository;

  private static final int RECORDS_PER_STREAM = 100;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<TaxonomyTermDTO.Response> getAllTaxonomyTerms(String search, Integer page,
      Integer perPage, String taxonomyId, Boolean topOnly, String parentId) {
      Specification<TaxonomyTerm> spec = buildSpecification(search, taxonomyId, topOnly, parentId);

      PageRequest pageable = PageRequest.of(page, perPage);
      Page<TaxonomyTerm> taxonomyTermPage = taxonomyTermRepository.findAll(spec, pageable);
      Page<TaxonomyTermDTO.Response> dtoPage = taxonomyTermPage.map(taxonomyTerm -> taxonomyTermMapper.toResponseDTO(taxonomyTerm, metadataService));
      return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  public void streamAllTaxonomyTerms(String search, String taxonomyId, Boolean topOnly,
      String parentId, Consumer<Response> consumer) {
    Specification<TaxonomyTerm> spec = buildSpecification(search, taxonomyId, topOnly, parentId);
    int currentPage = 0;
    boolean hasMoreData = true;
    while (hasMoreData) {
      PageRequest pageable = PageRequest.of(currentPage, RECORDS_PER_STREAM);
      Page<TaxonomyTerm> taxonomyTermPage = taxonomyTermRepository.findAll(spec, pageable);
      List<TaxonomyTerm> taxonomyTerms = taxonomyTermPage.getContent();
      if (taxonomyTerms.isEmpty()) {
        hasMoreData = false;
      } else {
        taxonomyTerms.forEach(taxonomyTerm ->
            consumer.accept(taxonomyTermMapper.toResponseDTO(taxonomyTerm, metadataService)));
        if (currentPage >= taxonomyTermPage.getTotalPages() - 1) {
          hasMoreData = false;
        } else {
          currentPage++;
        }
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Response getTaxonomyTermById(String id) {
    TaxonomyTerm taxonomyTerm = taxonomyTermRepository.findById(id).orElseThrow();
    return taxonomyTermMapper.toResponseDTO(taxonomyTerm, metadataService);
  }

  @Override
  @Transactional
  public Response createTaxonomyTerm(Request requestDto, String updatedBy) {
    if (requestDto.getId() == null || StringUtils.isBlank(requestDto.getId())) {
      requestDto.setId(UUID.randomUUID().toString());
    }
    TaxonomyTerm taxonomyTerm = taxonomyTermMapper.toEntity(requestDto);
    taxonomyTerm.setMetadata(metadataRepository, updatedBy);
    TaxonomyTerm savedTaxonomyTerm = taxonomyTermRepository.save(taxonomyTerm);
    return taxonomyTermMapper.toResponseDTO(savedTaxonomyTerm, metadataService);
  }

  @Override
  @Transactional
  public Response undoTaxonomyTermMetadata(String metadataId, String updatedBy) {
    Metadata metadata = this.metadataRepository.findById(metadataId)
            .orElseThrow(() -> new ResourceNotFoundException("Metadata", metadataId));

    TaxonomyTerm reverted = MetadataUtils.undoMetadata(
            metadata,
            this.metadataRepository,
            this.taxonomyTermRepository,
            TAXONOMY_TERM_FIELD_MAP,
            updatedBy
    );
    return taxonomyTermMapper.toResponseDTO(reverted, metadataService);
  }

  private Specification<TaxonomyTerm> buildSpecification(String search, String taxonomyId, Boolean topOnly, String parentId) {
    Specification<TaxonomyTerm> spec = Specification.where(null);

    if (search!= null && !search.isEmpty()) {
      spec = spec.and(TaxonomyTermSpecifications.hasSearchTerm(search));
    }

    if (taxonomyId != null && !taxonomyId.isEmpty()) {
      spec = spec.and(TaxonomyTermSpecifications.hasTaxonomyId(taxonomyId));
    }
    if (parentId != null && !parentId.isEmpty()) {
      spec = spec.and(TaxonomyTermSpecifications.hasParentId(parentId));
    }
    if (topOnly) {
      spec = spec.and(TaxonomyTermSpecifications.isLeaf());
    }

    return spec;
  }
}