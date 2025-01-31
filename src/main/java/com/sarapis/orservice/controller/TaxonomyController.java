package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.service.TaxonomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxonomies")
public class TaxonomyController {
    private final TaxonomyService taxonomyService;

    @Autowired
    public TaxonomyController(TaxonomyService taxonomyService) {
        this.taxonomyService = taxonomyService;
    }

  @GetMapping
  public ResponseEntity<PaginationDTO<TaxonomyDTO>> getAllTaxonomies(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                     @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
    List<TaxonomyDTO> taxonomyDTOs = this.taxonomyService.getAllTaxonomies();

    try {
      PaginationDTO<TaxonomyDTO> paginationDTO = PaginationDTO.of(
              taxonomyDTOs,
              page,
              perPage
      );
      return ResponseEntity.ok(paginationDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

    @GetMapping("/{taxonomyId}")
    public ResponseEntity<TaxonomyDTO> getTaxonomyById(@PathVariable String taxonomyId) {
        TaxonomyDTO taxonomy = this.taxonomyService.getTaxonomyById(taxonomyId);
        return ResponseEntity.ok(taxonomy);
    }

    @PostMapping
    public ResponseEntity<TaxonomyDTO> createTaxonomy(@RequestBody TaxonomyDTO taxonomyDTO) {
        TaxonomyDTO createdTaxonomy = this.taxonomyService.createTaxonomy(taxonomyDTO);
        return ResponseEntity.ok(createdTaxonomy);
    }

    @PutMapping("/{taxonomyId}")
    public ResponseEntity<TaxonomyDTO> updateTaxonomy(@PathVariable String taxonomyId,
                                                      @RequestBody TaxonomyDTO taxonomyDTO) {
        TaxonomyDTO updatedTaxonomy = this.taxonomyService.updateTaxonomy(taxonomyId, taxonomyDTO);
        return ResponseEntity.ok(updatedTaxonomy);
    }

    @DeleteMapping("/{taxonomyId}")
    public ResponseEntity<Void> deleteTaxonomy(@PathVariable String taxonomyId) {
        taxonomyService.deleteTaxonomy(taxonomyId);
        return ResponseEntity.noContent().build();
    }
}
