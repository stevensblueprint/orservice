package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import java.util.function.Consumer;

public interface TaxonomyTermService {
  PaginationDTO<TaxonomyTermDTO.Response> getAllTaxonomyTerms(
      String search,
      Integer page,
      Integer perPage,
      String taxonomyId,
      Boolean topOnly,
      String parentId
  );

  void streamAllTaxonomyTerms(String search, String taxonomyId, Boolean topOnly,
      String parentId, Consumer<TaxonomyTermDTO.Response> consumer);

  TaxonomyTermDTO.Response getTaxonomyTermById(String id);
  TaxonomyTermDTO.Response createTaxonomyTerm(TaxonomyTermDTO.Request requestDto, String updatedBy);
  TaxonomyTermDTO.Response undoTaxonomyTermMetadata(String metadataId);
}
