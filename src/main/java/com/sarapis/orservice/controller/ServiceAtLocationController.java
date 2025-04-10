package com.sarapis.orservice.controller;


import static com.sarapis.orservice.controller.Constants.JSON;
import static com.sarapis.orservice.controller.Constants.NDJSON;
import static com.sarapis.orservice.controller.Constants.NDJSON_APPLICATION_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.service.ServiceAtLocationService;
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
@RequestMapping("/service_at_locations")
@RequiredArgsConstructor
@Slf4j
public class ServiceAtLocationController {
  private final ServiceAtLocationService serviceAtLocationService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, NDJSON_APPLICATION_TYPE})
  public ResponseEntity<?> getAllServicesAtLocation(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "taxonomy_term_id", defaultValue = "") String taxonomyTermId,
      @RequestParam(name = "taxonomy_id", defaultValue = "") String taxonomyId,
      @RequestParam(name = "organization_id", defaultValue = "") String organizationId,
      @RequestParam(name = "modified_after", defaultValue = "") String modifiedAfter,
      @RequestParam(name = "full", defaultValue = "false") Boolean full,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format,
      @RequestParam(name = "postcode", defaultValue = "") String postcode,
      @RequestParam(name = "proximity", defaultValue = "") String proximity
  ) {
    return switch (format) {
      case (JSON) ->
        handleJsonResponse(search, taxonomyTermId, taxonomyId, organizationId, modifiedAfter, full, page, perPage, postcode, proximity);
      case (NDJSON) ->
        handleNdJsonResponse(search, taxonomyTermId, taxonomyId, organizationId, modifiedAfter, full, postcode, proximity);
      default -> ResponseEntity.badRequest().body(null);
    };
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO.Response> getServiceAtLocationById(@PathVariable String id) {
    return ResponseEntity.ok(serviceAtLocationService.getServiceAtLocationById(id));
  }

  @PostMapping
  public ResponseEntity<ServiceAtLocationDTO.Response> createServiceAtLocation(
      @Valid @RequestBody ServiceAtLocationDTO.Request request,
      @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(serviceAtLocationService.createServiceAtLocation(request, updatedBy));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceAtLocationDTO.Response> updateServiceAtLocation(
          @PathVariable String id,
          @Valid @RequestBody ServiceAtLocationDTO.Request request,
          @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.ok(this.serviceAtLocationService.updateServiceAtLocation(id, request, updatedBy));
  }

  private ResponseEntity<PaginationDTO<ServiceAtLocationDTO.Response>> handleJsonResponse(
      String search,
      String taxonomyTermId,
      String taxonomyId,
      String organizationId,
      String modifiedAfter,
      Boolean full,
      Integer page,
      Integer perPage,
      String postcode,
      String proximity
  ) {
    PaginationDTO<ServiceAtLocationDTO.Response> pagination = serviceAtLocationService.getAllServicesAtLocation(
        search,
        taxonomyTermId,
        taxonomyId,
        organizationId,
        modifiedAfter,
        full,
        page,
        perPage,
        postcode,
        proximity
    );
    return ResponseEntity.ok(pagination);
  }

  private ResponseEntity<StreamingResponseBody> handleNdJsonResponse(
      String search,
      String taxonomyTermId,
      String taxonomyId,
      String organizationId,
      String modifiedAfter,
      Boolean full,
      String postcode,
      String proximity
  ) {
    StreamingResponseBody responseBody = outputStream -> {
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
        ObjectMapper objectMapper = new ObjectMapper();
        serviceAtLocationService.streamAllServicesAtLocation(
            search, taxonomyTermId, taxonomyId, organizationId, modifiedAfter, full, postcode, proximity,
            serviceAtLocation -> {
              try {
                writer.write(objectMapper.writeValueAsString(serviceAtLocation));
                writer.write("\n");
                writer.flush();
              } catch (IOException e) {
                log.error("Error streaming organizations", e);
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
