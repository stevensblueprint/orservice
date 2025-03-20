package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
public interface TaxonomyTermService {
  PaginationDTO<TaxonomyTermDTO.Response> getAllTaxonomyTerms(
      String search,
      Integer page,
      Integer perPage,
      String format,
      String taxonomyId,
      Boolean topOnly,
      String parentId
  );

  TaxonomyTermDTO.Response getTaxonomyTermById(String id);
  TaxonomyTermDTO.Response createTaxonomyTerm(TaxonomyTermDTO.Request requestDto, String updatedBy);
}
