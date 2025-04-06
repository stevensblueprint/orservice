package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import java.util.function.Consumer;

public interface TaxonomyService {
  PaginationDTO<TaxonomyDTO.Response> getAllTaxonomies(
      String search,
      Integer page,
      Integer perPage
  );

  void streamAllTaxonomies(String search, Consumer<TaxonomyDTO.Response> consumer);

  TaxonomyDTO.Response getTaxonomyById(String id);
  TaxonomyDTO.Response createTaxonomy(TaxonomyDTO.Request requestDto, String updatedBy);
  TaxonomyDTO.Response undoTaxonomyMetadata(String metadataId);
}
