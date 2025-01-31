package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.exception.InvalidInputException;
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
  public ResponseEntity<PaginationDTO<TaxonomyTermDTO>> getAllTaxonomyTerms(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                            @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
    List<TaxonomyTermDTO> taxonomyTermDTOs = this.taxonomyTermService.getAllTaxonomyTerms();

    if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
    if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

    PaginationDTO<TaxonomyTermDTO> paginationDTO = PaginationDTO.of(
        taxonomyTermDTOs,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
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
