package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.TaxonomyDTO;
import java.util.List;

public interface TaxonomyService {
    List<TaxonomyDTO> getAllTaxonomies();

    TaxonomyDTO getTaxonomyById(String taxonomyId);

    TaxonomyDTO createTaxonomy(TaxonomyDTO taxonomyDTO);

    TaxonomyDTO updateTaxonomy(String taxonomyId, TaxonomyDTO taxonomyDTO);

    void deleteTaxonomy(String taxonomyId);
}
