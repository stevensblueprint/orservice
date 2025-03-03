package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.service.TaxonomyService;
import com.sarapis.orservice.service.TaxonomyServiceImpl;
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
@RequestMapping("/taxonomies")
@RequiredArgsConstructor
@Slf4j
public class TaxonomyController {
  private final TaxonomyService taxonomyService;

  @GetMapping
  public ResponseEntity<PaginationDTO<TaxonomyDTO.Response>> getAllTaxonomies(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "page", defaultValue = "1") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format
  ) {
    return ResponseEntity.ok(taxonomyService.getAllTaxonomies(
        search,
        page,
        perPage,
        format
    ));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyDTO.Response> getTaxonomyById(@PathVariable String id) {
    TaxonomyDTO.Response taxonomy = taxonomyService.getTaxonomyById(id);
    return ResponseEntity.ok(taxonomy);
  }

  @PostMapping
  public ResponseEntity<TaxonomyDTO.Response> createTaxonomy(@RequestBody @Valid TaxonomyDTO.Request requestDto) {
    TaxonomyDTO.Response taxonomy = taxonomyService.createTaxonomy(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(taxonomy);
  }
}
