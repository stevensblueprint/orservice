package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;

public interface TaxonomyService {
  PaginationDTO<TaxonomyDTO.Response> getAllTaxonomies(
      String search,
      Integer page,
      Integer perPage,
      String format
  );
  TaxonomyDTO.Response getTaxonomyById(String id);
  TaxonomyDTO.Response createTaxonomy(TaxonomyDTO.Request requestDto, String updatedBy);
}
