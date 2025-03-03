package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import java.util.List;

public interface TaxonomyTermService {
  List<Response> getAllTaxonomyTerms();
  TaxonomyTermDTO.Response getTaxonomyTermById(String id);
  TaxonomyTermDTO.Response createTaxonomyTerm(TaxonomyTermDTO.Request requestDto, String updatedBy);
  TaxonomyTermDTO.Response updateTaxonomyTerm(String id, TaxonomyTermDTO.UpdateRequest updateDto, String updatedBy);
  void deleteTaxonomyTerm(String id, String deletedBy);
}
