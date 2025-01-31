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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<PaginationDTO<TaxonomyDTO>> getAllTaxonomies(@RequestParam(defaultValue = "1") Integer page,
                                                                     @RequestParam(defaultValue = "10") Integer perPage) {
    List<TaxonomyDTO> taxonomyDTOs = this.taxonomyService.getAllTaxonomies();
    PaginationDTO<TaxonomyDTO> paginationDTO = PaginationDTO.of(
        taxonomyDTOs,
        page,
        perPage
    );
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyDTO> getTaxonomyById(@PathVariable String id) {
    return null;
  }

  @PostMapping
  public ResponseEntity<TaxonomyDTO> createTaxonomy(@RequestBody TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxonomyDTO> updateTaxonomy(@PathVariable String id, @RequestBody TaxonomyDTO taxonomyDTO) {
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaxonomy(@PathVariable String id) {
    taxonomyService.deleteTaxonomy(id);
    return ResponseEntity.noContent().build();
  }
}
