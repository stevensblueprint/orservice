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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    PaginationDTO<TaxonomyTermDTO> paginationDTO = PaginationDTO.of(
        taxonomyTermDTOs,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO> getTaxonomyTermById(@PathVariable String id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<TaxonomyTermDTO> createTaxonomyTerm(@RequestBody TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO> updateTaxonomyTerm(@PathVariable String id, @RequestBody TaxonomyTermDTO taxonomyTermDTO) {
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaxonomyTerm(@PathVariable String id) {
    taxonomyTermService.deleteTaxonomyTerm(id);
    return ResponseEntity.noContent().build();
  }


}
