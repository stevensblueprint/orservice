package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.dto.TaxonomyDTO.Response;
import java.util.List;

public interface TaxonomyService {
  List<Response> getAllTaxonomies();
  TaxonomyDTO.Response getTaxonomyById(String id);
  TaxonomyDTO.Response createTaxonomy(TaxonomyDTO.Request requestDto, String updatedBy);
  TaxonomyDTO.Response updateTaxonomy(String id, TaxonomyDTO.UpdateRequest updateDto, String updatedBy);
  void deleteTaxonomy(String id, String deletedBy);
}
