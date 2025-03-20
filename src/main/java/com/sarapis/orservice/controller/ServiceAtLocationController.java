package com.sarapis.orservice.controller;


import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.ServiceAtLocationDTO;
import com.sarapis.orservice.service.ServiceAtLocationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service_at_locations")
@RequiredArgsConstructor
@Slf4j
public class ServiceAtLocationController {
  private final ServiceAtLocationService serviceAtLocationService;

  @GetMapping
  public ResponseEntity<PaginationDTO<ServiceAtLocationDTO.Response>> getAllServicesAtLocation(
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
    PaginationDTO<ServiceAtLocationDTO.Response> pagination = serviceAtLocationService.getAllServicesAtLocation(
        search,
        taxonomyTermId,
        taxonomyId,
        organizationId,
        modifiedAfter,
        full,
        page,
        perPage,
        format,
        postcode,
        proximity
    );
    return ResponseEntity.ok(pagination);
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
}
