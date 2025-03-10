package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.dto.TaxonomyDTO.Request;
import com.sarapis.orservice.dto.TaxonomyDTO.Response;
import com.sarapis.orservice.mapper.TaxonomyMapper;
import com.sarapis.orservice.model.Taxonomy;
import com.sarapis.orservice.repository.TaxonomyRepository;
import com.sarapis.orservice.repository.TaxonomySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxonomyServiceImpl implements  TaxonomyService {

  private final TaxonomyRepository taxonomyRepository;
  private final TaxonomyMapper taxonomyMapper;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<Response> getAllTaxonomies(String search, Integer page, Integer perPage, String format) {
    Specification<Taxonomy> spec = Specification.where(null);
    if (search != null && !search.isEmpty()) {
      spec = spec.and(TaxonomySpecifications.hasSearchTerm(search));
    }

    PageRequest pageable = PageRequest.of(page, perPage);
    Page<Taxonomy> taxonomyPage = taxonomyRepository.findAll(spec, pageable);
    Page<TaxonomyDTO.Response> dtoPage = taxonomyPage.map(taxonomyMapper::toResponseDTO);
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public Response getTaxonomyById(String id) {
    Taxonomy taxonomy = taxonomyRepository.findById(id).orElseThrow();
    return taxonomyMapper.toResponseDTO(taxonomy);
  }

  @Override
  public Response createTaxonomy(Request requestDto) {
    return null;
  }
}
