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
      @RequestParam(name = "page", defaultValue = "1")
      @Min(value = 1, message = "Invalid input provided for page")
      int page,
      @RequestParam(name = "perPage", defaultValue = "10")
      @Min(value = 1, message = "Invalid input provided for perPage")
      int perPage
  ) {
    log.info("Received request to get all TaxonomyTerms");
    List<Response> taxonomyTerms = taxonomyTermService.getAllTaxonomyTerms();
    PaginationDTO<Response> paginationDTO = PaginationDTO.of(taxonomyTerms, page, perPage);
    log.info("Returning {} TaxonomyTerms", taxonomyTerms.size());
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO.Response> getTaxonomyTermById(@PathVariable String id) {
    log.info("Received request to get TaxonomyTerm by id: {}", id);
    TaxonomyTermDTO.Response taxonomyTerm = taxonomyTermService.getTaxonomyTermById(id);
    log.info("Returning URL with id: {}", id);
    return ResponseEntity.ok(taxonomyTerm);
  }

  @PostMapping
  public ResponseEntity<TaxonomyTermDTO.Response> createTaxonomyTerm(
      @Valid @RequestBody TaxonomyTermDTO.Request requestDto,
      @RequestHeader("X-Updated-By") String updatedBy
  ) {
    log.info("Received request to create TaxonomyTerm with data: {}", requestDto);
    TaxonomyTermDTO.Response createdTaxonomyTerm = taxonomyTermService.createTaxonomyTerm(requestDto, updatedBy);
    log.info("Created TaxonomyTerm with id: {}", createdTaxonomyTerm.getId());
    return new ResponseEntity<>(createdTaxonomyTerm, org.springframework.http.HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> deleteUrl(
      @PathVariable String id,
      @RequestHeader("X-Deleted-By") String deletedBy
  ) {
    log.info("Received request to delete TaxonomyTerm with id: {} by user: {}", id, deletedBy);
    taxonomyTermService.deleteTaxonomyTerm(id, deletedBy);
    log.info("Deleted TaxonomyTerm with id: {}", id);
    return ResponseEntity.noContent().build();
  }
}
