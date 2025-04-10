package com.sarapis.orservice.controller;

import static com.sarapis.orservice.controller.Constants.JSON;
import static com.sarapis.orservice.controller.Constants.NDJSON;
import static com.sarapis.orservice.controller.Constants.NDJSON_APPLICATION_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.service.ServiceService;
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
@RequestMapping("/services")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {
  private final ServiceService serviceService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, NDJSON_APPLICATION_TYPE})
  public ResponseEntity<?> getAllServices(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format,
      @RequestParam(name = "taxonomy_term_id", defaultValue = "") String taxonomyTermId,
      @RequestParam(name = "taxonomy_id", defaultValue = "") String taxonomyId,
      @RequestParam(name = "organization_id", defaultValue = "") String organizationId,
      @RequestParam(name = "modified_after", defaultValue = "") String modifiedAfter,
      @RequestParam(name = "minimal", defaultValue = "false") Boolean minimal,
      @RequestParam(name = "full", defaultValue = "false") Boolean full
  ) {
    return switch (format) {
      case (JSON) ->
          handleJsonResponse(search, page, perPage, taxonomyTermId, taxonomyId, organizationId, modifiedAfter, minimal, full);
      case (NDJSON) ->
        handleNdJsonResponse(search, taxonomyTermId, taxonomyId, organizationId, modifiedAfter, minimal, full);
      default ->
         ResponseEntity.badRequest().body(null);
    };
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceDTO.Response> getServiceById(@PathVariable String id) {
    return ResponseEntity.ok(serviceService.getServiceById(id));
  }

  @PostMapping
  public ResponseEntity<ServiceDTO.Response> createService(
      @Valid @RequestBody ServiceDTO.Request servicedDto,
      @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createService(servicedDto, updatedBy));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ServiceDTO.Response> updateService(
          @PathVariable String id,
          @Valid @RequestBody ServiceDTO.Request request,
          @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.ok(this.serviceService.updateService(id, request, updatedBy));
  }

  private ResponseEntity<PaginationDTO<ServiceDTO.Response>> handleJsonResponse(
      String search,
      Integer page,
      Integer perPage,
      String taxonomyTermId,
      String taxonomyId,
      String organizationId,
      String modifiedAfter,
      Boolean minimal,
      Boolean full
  ) {
    return ResponseEntity.ok(serviceService.getAllServices(
        search,
        page,
        perPage,
        taxonomyTermId,
        taxonomyId,
        organizationId,
        modifiedAfter,
        minimal,
        full
    ));
  }

  private ResponseEntity<StreamingResponseBody> handleNdJsonResponse(String search, String taxonomyTermId, String taxonomyId, String organizationId,
      String modifiedAfter, Boolean minimal, Boolean full) {
    StreamingResponseBody responseBody = outputStream -> {
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
        ObjectMapper objectMapper = new ObjectMapper();
        serviceService.streamAllServices(
            search, taxonomyTermId, taxonomyId, organizationId,  modifiedAfter,  minimal, full,
            service -> {
              try {
                writer.write(objectMapper.writeValueAsString(service));
                writer.write("\n");
              } catch (IOException e) {
                log.error("Failed to serialize service to NDJSON", e);
              }
            }
        );
      } catch (IOException e) {
        log.error("Failed to write to output stream", e);
      }
    };
    return ResponseEntity.ok()
        .contentType(MediaType.valueOf("application/x-ndjson"))
        .body(responseBody);
  }
}
