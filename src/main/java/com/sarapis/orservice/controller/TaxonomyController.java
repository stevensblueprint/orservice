package com.sarapis.orservice.controller;

import static com.sarapis.orservice.controller.Constants.JSON;
import static com.sarapis.orservice.controller.Constants.NDJSON;
import static com.sarapis.orservice.controller.Constants.NDJSON_APPLICATION_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.TaxonomyDTO;
import com.sarapis.orservice.service.TaxonomyService;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/taxonomies")
@RequiredArgsConstructor
@Slf4j
public class TaxonomyController {
  private final TaxonomyService taxonomyService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, NDJSON_APPLICATION_TYPE})
  public ResponseEntity<?> getAllTaxonomies(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format
  ) {
    return switch (format) {
      case (JSON) ->
        handleJsonResponse(search, page, perPage);
      case (NDJSON) ->
        handleNdjsonResponse(search);
      default -> ResponseEntity.badRequest().body(null);
    };
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaxonomyDTO.Response> getTaxonomyById(@PathVariable String id) {
    TaxonomyDTO.Response taxonomy = taxonomyService.getTaxonomyById(id);
    return ResponseEntity.ok(taxonomy);
  }

  @PostMapping
  public ResponseEntity<TaxonomyDTO.Response> createTaxonomy(
      @RequestBody @Valid TaxonomyDTO.Request requestDto,
      @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
      ) {
    TaxonomyDTO.Response taxonomy = taxonomyService.createTaxonomy(requestDto, updatedBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(taxonomy);
  }

  @PostMapping("/undo/{metadataId}")
  public ResponseEntity<Void> undoTaxonomyMetadata(@PathVariable String metadataId) {
    taxonomyService.undoTaxonomyMetadata(metadataId);
    return ResponseEntity.noContent().build();
  }

  private ResponseEntity<PaginationDTO<TaxonomyDTO.Response>> handleJsonResponse(
      String search, Integer page, Integer perPage
  ) {
    PaginationDTO<TaxonomyDTO.Response> pagination = taxonomyService.getAllTaxonomies(
        search,
        page,
        perPage
    );
    return ResponseEntity.ok(pagination);
  }

  private ResponseEntity<StreamingResponseBody> handleNdjsonResponse(String search) {
    StreamingResponseBody responseBody = outputStream -> {
      try (Writer write = new BufferedWriter(new OutputStreamWriter(outputStream))) {
        ObjectMapper objectMapper = new ObjectMapper();
        taxonomyService.streamAllTaxonomies(search, taxonomy -> {
          try {
            write.write(objectMapper.writeValueAsString(taxonomy));
            write.write("\n");
            write.flush();
          } catch (IOException e) {
            log.error("Error writing taxonomy to NDJSON: {}", e.getMessage(), e);
          }
        }
        );
      } catch (IOException e) {
        log.error("Error streaming taxonomies: {}", e.getMessage(), e);
      }
    };
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_NDJSON).body(responseBody);
  }
}
