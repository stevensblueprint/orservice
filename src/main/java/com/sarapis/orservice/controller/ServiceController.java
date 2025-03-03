package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceDTO;
import com.sarapis.orservice.service.ServiceService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {
  private final ServiceService service;

  @GetMapping
  public ResponseEntity<PaginationDTO<ServiceDTO.Response>> getAllServices(
      @RequestParam(name = "search") String search,
      @RequestParam(name = "page", defaultValue = "1") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format,
      @RequestParam(name = "taxonomy_term_id") String taxonomyTermId,
      @RequestParam(name = "taxonomy_id") String taxonomyId,
      @RequestParam(name = "organization_id") String organizationId,
      @RequestParam(name = "modified_after") String modifiedAfter,
      @RequestParam(name = "minimal", defaultValue = "false") Boolean minimal,
      @RequestParam(name = "full", defaultValue = "false") Boolean full
  ) {
    return ResponseEntity.ok(service.getAllServices(
        search,
        page,
        perPage,
        format,
        taxonomyTermId,
        taxonomyId,
        organizationId,
        modifiedAfter,
        minimal,
        full
    ));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceDTO.Response> getServiceById(@PathVariable String id) {
    return ResponseEntity.ok(service.getServiceById(id));
  }

  @PostMapping
  public ResponseEntity<ServiceDTO.Response> createService(
      @Valid @RequestBody ServiceDTO.Request servicedDto
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.createService(servicedDto));
  }
}
