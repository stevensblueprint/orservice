package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.service.TaxonomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/taxonomies")
public class TaxonomyController {
  private final TaxonomyService taxonomyService;

  @Autowired
  public TaxonomyController(TaxonomyService taxonomyService) {
    this.taxonomyService = taxonomyService;
  }

  @GetMapping
  public ResponseEntity<PaginationDTO<TaxonomyDTO>> getAllTaxonomies() {
    return null;
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyDTO> getTaxonomyById(@PathVariable Long id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<TaxonomyDTO> createTaxonomy(@RequestBody TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxonomyDTO> updateTaxonomy(@PathVariable Long id, @RequestBody TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaxonomy(@PathVariable Long id) {
    taxonomyService.deleteTaxonomy(id);
    return ResponseEntity.noContent().build();
  }
}
