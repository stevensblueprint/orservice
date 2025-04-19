package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.model.Metadata;

import java.util.List;
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
  TaxonomyTermDTO.Response updateTaxonomyTerm(String id, TaxonomyTermDTO.Request updateDto, String updatedBy);
  TaxonomyTermDTO.Response undoTaxonomyTermMetadata(String metadataId, String updatedBy);
  TaxonomyTermDTO.Response undoTaxonomyTermMetadataBatch(List<Metadata> metadataList, String updatedBy);
}
