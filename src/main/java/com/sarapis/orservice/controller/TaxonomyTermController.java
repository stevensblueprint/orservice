package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO.Response;
import com.sarapis.orservice.service.TaxonomyTermService;
import com.sarapis.orservice.service.TaxonomyTermServiceImpl;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/taxonomyTerms")
@RequiredArgsConstructor
@Slf4j
public class TaxonomyTermController {
  private final TaxonomyTermService taxonomyTermService;

  @GetMapping
  public ResponseEntity<PaginationDTO<TaxonomyTermDTO.Response>> getAllTaxonomyTerms(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format,
      @RequestParam(name = "taxonomy_id") String taxonomyId,
      @RequestParam(name = "top_only", defaultValue = "false") boolean topOnly,
      @RequestParam(name = "parent_id") String parentId
  ) {
    return ResponseEntity.ok(taxonomyTermService.getAllTaxonomyTerms(
        search,
        page,
        perPage,
        format,
        taxonomyId,
        topOnly,
        parentId
    ));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO.Response> getTaxonomyTermById(@PathVariable String id) {
    TaxonomyTermDTO.Response taxonomyTerm = taxonomyTermService.getTaxonomyTermById(id);
    return ResponseEntity.ok(taxonomyTerm);
  }

  @PostMapping
  public ResponseEntity<TaxonomyTermDTO.Response> createTaxonomyTerm(
      @Valid @RequestBody TaxonomyTermDTO.Request taxonomyTerm
  ) {
    return ResponseEntity.ok(taxonomyTermService.createTaxonomyTerm(taxonomyTerm));
  }

}
