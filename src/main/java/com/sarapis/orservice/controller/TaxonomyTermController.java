package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
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
  public ResponseEntity<PaginationDTO<TaxonomyTermDTO>> getAllTaxonomyTerms(@RequestParam(defaultValue = "1") Integer page,
                                                                            @RequestParam(defaultValue = "10") Integer perPage) {
    List<TaxonomyTermDTO> taxonomyTermDTOs = this.taxonomyTermService.getAllTaxonomyTerms();

    try {
      PaginationDTO<TaxonomyTermDTO> paginationDTO = PaginationDTO.of(
              taxonomyTermDTOs,
              page,
              perPage
      );
      return ResponseEntity.ok(paginationDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
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
