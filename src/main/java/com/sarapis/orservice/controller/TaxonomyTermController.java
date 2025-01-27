package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.service.TaxonomyTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxonomy_terms")
public class TaxonomyTermController {
    private final TaxonomyTermService taxonomyTermService;

    @Autowired
    public TaxonomyTermController(TaxonomyTermService taxonomyTermService) {
        this.taxonomyTermService = taxonomyTermService;
    }

    @GetMapping
    public ResponseEntity<List<TaxonomyTermDTO>> getAllTaxonomyTerms() {
        List<TaxonomyTermDTO> taxonomyTerms = this.taxonomyTermService.getAllTaxonomyTerms();
        return ResponseEntity.ok(taxonomyTerms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxonomyTermDTO> getTaxonomyTermById(@PathVariable String id) {
        TaxonomyTermDTO taxonomyTerm = this.taxonomyTermService.getTaxonomyTermById(id);
        return ResponseEntity.ok(taxonomyTerm);
    }

    @PostMapping
    public ResponseEntity<TaxonomyTermDTO> createTaxonomyTerm(@RequestBody TaxonomyTermDTO taxonomyTermDTO) {
        TaxonomyTermDTO createdTaxonomyTerm = this.taxonomyTermService.createTaxonomyTerm(taxonomyTermDTO);
        return ResponseEntity.ok(createdTaxonomyTerm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxonomyTermDTO> updateTaxonomyTerm(@PathVariable String id,
                                                              @RequestBody TaxonomyTermDTO taxonomyTermDTO) {
        TaxonomyTermDTO updatedTaxonomyTerm = this.taxonomyTermService.updateTaxonomyTerm(id, taxonomyTermDTO);
        return ResponseEntity.ok(updatedTaxonomyTerm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxonomyTerm(@PathVariable String id) {
        this.taxonomyTermService.deleteTaxonomyTerm(id);
        return ResponseEntity.noContent().build();
    }
}
