package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.entity.Taxonomy;
import com.sarapis.orservice.entity.TaxonomyTerm;
import com.sarapis.orservice.repository.TaxonomyTermRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyTermServiceImpl implements TaxonomyTermService {
  private final TaxonomyTermRepository taxonomyTermRepository;

  @Autowired
  public TaxonomyTermServiceImpl(TaxonomyTermRepository taxonomyTermRepository) {
    this.taxonomyTermRepository = taxonomyTermRepository;
  }

  private TaxonomyTermServiceImpl mapToDTO(TaxonomyTerm taxonomyTerm) {
    return null;
  }

  private Taxonomy mapToEntity(TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @Override
  public List<TaxonomyTermDTO> getAllTaxonomyTerms() {
    return List.of();
  }

  @Override
  public TaxonomyTermDTO getTaxonomyTermById(String id) {
    return null;
  }

  @Override
  public TaxonomyTermDTO createTaxonomyTerm(TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @Override
  public TaxonomyTermDTO updateTaxonomyTerm(String id, TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @Override
  public void deleteTaxonomyTerm(String id) {

  }
}
