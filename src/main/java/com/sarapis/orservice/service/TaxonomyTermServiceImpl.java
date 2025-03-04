package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Request;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.TaxonomyTermMapper;
import com.sarapis.orservice.model.TaxonomyTerm;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import com.sarapis.orservice.repository.TaxonomyTermSpecifications;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

@Service
@RequiredArgsConstructor
public class TaxonomyTermServiceImpl implements TaxonomyTermService {
  private final TaxonomyTermRepository taxonomyTermRepository;
  private final TaxonomyTermMapper taxonomyTermMapper;


  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<TaxonomyTermDTO.Response> getAllTaxonomyTerms(String search, Integer page,
      Integer perPage, String format, String taxonomyId, Boolean topOnly, String parentId) {
      Specification<TaxonomyTerm> spec = Specification.where(null);
      if (search != null && !search.isEmpty()) {
        spec = spec.and(TaxonomyTermSpecifications.hasSearchTerm(search));
      }

      PageRequest pageable = PageRequest.of(page, perPage);
      Page<TaxonomyTerm> taxonomyTermPage = taxonomyTermRepository.findAll(spec, pageable);
      Page<TaxonomyTermDTO.Response> dtoPage = taxonomyTermPage.map(taxonomyTermMapper::toResponseDTO);
      return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getTaxonomyTermById(String id) {
    TaxonomyTerm taxonomyTerm = taxonomyTermRepository.findById(id).orElseThrow();
    return taxonomyTermMapper.toResponseDTO(taxonomyTerm);
  }

  @Override
  @Transactional
  public Response createTaxonomyTerm(Request requestDto) {
    return null;
  }
}