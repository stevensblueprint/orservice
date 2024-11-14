package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.entity.Taxonomy;
import com.sarapis.orservice.repository.TaxonomyRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyServiceImpl implements TaxonomyService {
  private final TaxonomyRepository taxonomyRepository;

  @Autowired
  public TaxonomyServiceImpl(TaxonomyRepository taxonomyRepository) {
    this.taxonomyRepository = taxonomyRepository;
  }

  private TaxonomyDTO mapToDTO(Taxonomy taxonomy) {
    return null;
  }

  private Taxonomy mapToEntity(TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @Override
  public List<TaxonomyDTO> getAllTaxonomies() {
    return List.of();
  }

  @Override
  public TaxonomyDTO getTaxonomyById(String id) {
    return null;
  }

  @Override
  public TaxonomyDTO createTaxonomy(TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @Override
  public TaxonomyDTO updateTaxonomy(String id, TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @Override
  public void deleteTaxonomy(String id) {

  }
}
