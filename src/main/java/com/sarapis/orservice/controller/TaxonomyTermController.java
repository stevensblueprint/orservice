package com.sarapis.orservice.controller;

import static com.sarapis.orservice.controller.Constants.JSON;
import static com.sarapis.orservice.controller.Constants.NDJSON;
import static com.sarapis.orservice.controller.Constants.NDJSON_APPLICATION_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyTermDTO;
import com.sarapis.orservice.service.TaxonomyTermService;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/taxonomy_terms")
@RequiredArgsConstructor
@Slf4j
public class TaxonomyTermController {
  private final TaxonomyTermService taxonomyTermService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, NDJSON_APPLICATION_TYPE})
  public ResponseEntity<?> getAllTaxonomyTerms(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format,
      @RequestParam(name = "taxonomy_id", defaultValue = "") String taxonomyId,
      @RequestParam(name = "top_only", defaultValue = "false") boolean topOnly,
      @RequestParam(name = "parent_id", defaultValue = "") String parentId
  ) {
    return switch (format) {
      case (JSON) ->
        handleJsonResponse(search, page, perPage, taxonomyId, topOnly, parentId);
      case (NDJSON) ->
        handleNdjsonResponse(search, taxonomyId, topOnly, parentId);
      default -> ResponseEntity.badRequest().body(null);
    };
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyTermDTO.Response> getTaxonomyTermById(@PathVariable String id) {
    TaxonomyTermDTO.Response taxonomyTerm = taxonomyTermService.getTaxonomyTermById(id);
    return ResponseEntity.ok(taxonomyTerm);
  }

  @PostMapping
  public ResponseEntity<TaxonomyTermDTO.Response> createTaxonomyTerm(
      @Valid @RequestBody TaxonomyTermDTO.Request taxonomyTerm,
      @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.ok(taxonomyTermService.createTaxonomyTerm(taxonomyTerm, updatedBy));
  }

  private ResponseEntity<PaginationDTO<TaxonomyTermDTO.Response>> handleJsonResponse(
      String search,
      Integer page,
      Integer perPage,
      String taxonomyId,
      boolean topOnly,
      String parentId
  ) {
    PaginationDTO<TaxonomyTermDTO.Response> pagination = taxonomyTermService.getAllTaxonomyTerms(
        search,
        page,
        perPage,
        taxonomyId,
        topOnly,
        parentId
    );
    return ResponseEntity.ok(pagination);
  }

  private ResponseEntity<StreamingResponseBody> handleNdjsonResponse(
      String search,
      String taxonomyId,
      boolean topOnly,
      String parentId
  ) {
    StreamingResponseBody responseBody = outputStream -> {
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
        ObjectMapper objectMapper = new ObjectMapper();
        taxonomyTermService.streamAllTaxonomyTerms(
            search, taxonomyId, topOnly, parentId,
            taxonomyTerm -> {
              try {
                writer.write(objectMapper.writeValueAsString(taxonomyTerm));
                writer.write("\n");
                writer.flush();
              } catch (IOException e) {
                log.error("Error writing taxonomy term to stream", e);
              }
            }
        );
      } catch (IOException e) {
        log.error("Error streaming taxonomy terms: {}", e.getMessage(), e);
      }
    };
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_NDJSON).body(responseBody);
  }

}
