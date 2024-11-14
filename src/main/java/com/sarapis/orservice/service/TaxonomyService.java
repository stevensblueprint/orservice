package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import java.util.List;

public interface TaxonomyService {
  List<TaxonomyDTO> getAllTaxonomies();

  TaxonomyDTO getTaxonomyById(Long id);

  TaxonomyDTO createTaxonomy(TaxonomyDTO taxonomyDTO);

  TaxonomyDTO updateTaxonomy(Long id, TaxonomyDTO taxonomyDTO);

  void deleteTaxonomy(Long id);
}
