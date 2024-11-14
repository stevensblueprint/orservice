package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import java.util.List;

public interface TaxonomyTermService {
  List<TaxonomyTermDTO> getAllTaxonomyTerms();

  TaxonomyTermDTO getTaxonomyTermById(String id);

  TaxonomyTermDTO createTaxonomyTerm(TaxonomyTermDTO taxonomyTermDTO);

  TaxonomyTermDTO updateTaxonomyTerm(String id, TaxonomyTermDTO taxonomyTermDTO);

  void deleteTaxonomyTerm(String id);
}
