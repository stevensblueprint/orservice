package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import java.util.List;

public interface TaxonomyService {
  List<TaxonomyDTO> getAllTaxonomies();

  TaxonomyDTO getTaxonomyById(String id);

  TaxonomyDTO createTaxonomy(TaxonomyDTO taxonomyDTO);

  TaxonomyDTO updateTaxonomy(String id, TaxonomyDTO taxonomyDTO);

  void deleteTaxonomy(String id);
}
