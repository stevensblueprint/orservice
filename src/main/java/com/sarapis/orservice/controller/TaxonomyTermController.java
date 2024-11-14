package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.service.TaxonomyTermService;
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
@RequestMapping("/api/taxonomy_terms")
public class TaxonomyTermController {
  private final TaxonomyTermService taxonomyTermService;

  @Autowired
  public TaxonomyTermController(TaxonomyTermService taxonomyTermService) {
    this.taxonomyTermService = taxonomyTermService;
  }

  @GetMapping
  public ResponseEntity<PaginationDTO<TaxonomyTermDTO>> getAllTaxonomyTerms() {
    return null;
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO> getTaxonomyTermById(@PathVariable Long id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<TaxonomyTermDTO> createTaxonomyTerm(@RequestBody TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO> updateTaxonomyTerm(@PathVariable Long id, @RequestBody TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaxonomyTerm(@PathVariable Long id) {
    taxonomyTermService.deleteTaxonomyTerm(id);
    return ResponseEntity.noContent().build();
  }


}
