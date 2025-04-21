package com.sarapis.orservice.controller;

import static com.sarapis.orservice.controller.Constants.JSON;
import static com.sarapis.orservice.controller.Constants.NDJSON;
import static com.sarapis.orservice.controller.Constants.NDJSON_APPLICATION_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapis.orservice.config.PublicEndpoint;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.OrganizationService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {
  private final OrganizationService organizationService;


  @PublicEndpoint
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, NDJSON_APPLICATION_TYPE})
  public ResponseEntity<?> getAllOrganizations(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "full_service", defaultValue = "false") Boolean fullService,
      @RequestParam(name = "full", defaultValue = "true") Boolean full,
      @RequestParam(name = "taxonomy_term_id", defaultValue = "") String taxonomyTermId,
      @RequestParam(name = "taxonomy_id", defaultValue = "") String taxonomyId,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format
  ) {
    return switch (format) {
      case (JSON) ->
          handleJsonResponse(search, fullService, full, taxonomyTermId, taxonomyId, page, perPage);
      case (NDJSON) ->
          handleNdjsonResponse(search, fullService, full, taxonomyTermId, taxonomyId);
      default -> ResponseEntity.badRequest().body(null);
    };
  }

  @PublicEndpoint
  @GetMapping("/{id}")
  public ResponseEntity<OrganizationDTO.Response> getServiceById(
      @PathVariable String id,
      @RequestParam(name = "full_service", defaultValue = "true") Boolean fullService) {
    return ResponseEntity.ok(organizationService.getOrganizationById(id, fullService));
  }

  @PostMapping
  public ResponseEntity<OrganizationDTO.Response> createOrganization(
      @Valid @RequestBody OrganizationDTO.Request request,
      @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.createOrganization(request, updatedBy));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrganizationDTO.Response> updateOrganization(
          @PathVariable String id,
          @Valid @RequestBody OrganizationDTO.Request request,
          @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.ok(organizationService.updateOrganization(id, request, updatedBy));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
    organizationService.deleteOrganization(id);
    return ResponseEntity.noContent().build();
  }

  private ResponseEntity<PaginationDTO<OrganizationDTO.Response>> handleJsonResponse(
      String search, Boolean fullService, Boolean full, String taxonomyTermId, String taxonomyId,
      Integer page, Integer perPage
  ) {
    PaginationDTO<OrganizationDTO.Response> pagination = organizationService.getAllOrganizations(
        search,
        fullService,
        full,
        taxonomyTermId,
        taxonomyId,
        page,
        perPage
    );

    return ResponseEntity.ok(pagination);
  }

  private ResponseEntity<StreamingResponseBody> handleNdjsonResponse(
          String search, Boolean fullService, Boolean full,
          String taxonomyTermId, String taxonomyId
  ) {
    StreamingResponseBody responseBody = outputStream -> {
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
        ObjectMapper objectMapper = new ObjectMapper();

        organizationService.streamAllOrganizations(
            search, fullService, full, taxonomyTermId, taxonomyId,
            organization -> {
              try {
                writer.write(objectMapper.writeValueAsString(organization));
                writer.write("\n");
                writer.flush();
              } catch (IOException e) {
                log.error("Error writing organization to stream", e);
              }
            }
        );
      } catch (IOException e) {
        log.error("Error streaming organizations", e);
      }
    };

    return ResponseEntity.ok()
        .contentType(MediaType.valueOf("application/x-ndjson"))
        .body(responseBody);
  }
}
