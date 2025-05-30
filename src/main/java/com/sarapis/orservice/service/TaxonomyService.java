package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.model.Metadata;

import java.util.List;
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
  TaxonomyDTO.Response updateTaxonomy(String id, TaxonomyDTO.Request updateDTO, String updatedBy);
  TaxonomyDTO.Response undoTaxonomyMetadata(String metadataId, String updatedBy);
  TaxonomyDTO.Response undoTaxonomyMetadataBatch(List<Metadata> metadataList, String updatedBy);
}
