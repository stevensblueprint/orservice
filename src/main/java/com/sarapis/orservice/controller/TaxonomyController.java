package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.service.TaxonomyService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/taxonomies")
@RequiredArgsConstructor
@Slf4j
public class TaxonomyController {
  private final TaxonomyService taxonomyService;

  @GetMapping
  public ResponseEntity<PaginationDTO<TaxonomyDTO.Response>> getAllTaxonomies(
      @RequestParam(name = "page", defaultValue = "1")
      @Min(value = 1, message = "Invalid input provided for 'page'.")
      int page,
      @RequestParam(name = "perPage", defaultValue = "10")
      @Min(value = 1, message = "Invalid input provided for 'perPage'.")
      int perPage
  ) {
    log.info("Received request to get all taxonomies");
    List<TaxonomyDTO.Response> taxonomies = taxonomyService.getAllTaxonomies();
    PaginationDTO<TaxonomyDTO.Response> paginationDTO = PaginationDTO.of(taxonomies, page, perPage);
    log.info("Returning {} taxonomies", taxonomies.size());
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyDTO.Response> getTaxonomyById(@PathVariable String id) {
    log.info("Received request to get taxonomy by id: {}", id);
    TaxonomyDTO.Response taxonomy = taxonomyService.getTaxonomyById(id);
    log.info("Returning taxonomy with id: {}", id);
    return ResponseEntity.ok(taxonomy);
  }

  @PostMapping
  public ResponseEntity<TaxonomyDTO.Response> createTaxonomy(@Valid @RequestBody TaxonomyDTO.Request requestDto) {
    log.info("Received request to create taxonomy with data: {}", requestDto);
    TaxonomyDTO.Response createdTaxonomy = taxonomyService.createTaxonomy(requestDto);
    log.info("Created taxonomy with id: {}", createdTaxonomy.getId());
    return new ResponseEntity<>(createdTaxonomy, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxonomyDTO.Response> updateTaxonomy(
      @PathVariable String id,
      @Valid @RequestBody TaxonomyDTO.UpdateRequest updateDto,
      @RequestHeader("X-Updated-By") String updatedBy
  ) {
    log.info("Received request to update Taxonomy with id: {} by user: {}", id, updateDto);
    TaxonomyDTO.Response updatedTaxonomy = taxonomyService.updateTaxonomy(id, updateDto, updatedBy);
    log.info("Updated taxonomy with id: {}", id);
    return ResponseEntity.ok(updatedTaxonomy);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaxonomy(
      @PathVariable String id,
      @RequestHeader("X-Deleted-By") String deletedBy
  ) {
    log.info("Received request to delete Taxonomy with id: {} by user: {}", id, deletedBy);
    taxonomyService.deleteTaxonomy(id, deletedBy);
    log.info("Deleted Taxonomy with id: {}", id);
    return ResponseEntity.noContent().build();
  }
}
