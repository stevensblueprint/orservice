package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import java.util.List;

public interface TaxonomyTermService {
  List<TaxonomyTermDTO> getAllTaxonomyTerms();

  TaxonomyTermDTO getTaxonomyTermById(Long id);

  TaxonomyTermDTO createTaxonomyTerm(TaxonomyTermDTO taxonomyTermDTO);

  TaxonomyTermDTO updateTaxonomyTerm(Long id, TaxonomyTermDTO taxonomyTermDTO);

  void deleteTaxonomyTerm(Long id);
}
