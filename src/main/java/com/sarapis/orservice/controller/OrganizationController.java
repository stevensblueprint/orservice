package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.OrganizationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {
  private final OrganizationService organizationService;

  @GetMapping
  public ResponseEntity<PaginationDTO<OrganizationDTO.Response>> getAllOrganizations(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "full_service", defaultValue = "false") Boolean fullService,
      @RequestParam(name = "full", defaultValue = "true") Boolean full,
      @RequestParam(name = "taxonomy_term_id", defaultValue = "") String taxonomyTermId,
      @RequestParam(name = "taxonomy_id", defaultValue = "") String taxonomyId,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", defaultValue = "10") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format
  ) {
    PaginationDTO<OrganizationDTO. Response> pagination = organizationService.getAllOrganizations(
        search,
        fullService,
        full,
        taxonomyTermId,
        taxonomyId,
        page,
        perPage,
        format
    );

    return ResponseEntity.ok(pagination);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrganizationDTO.Response> getServiceById(@PathVariable String id) {
    return ResponseEntity.ok(organizationService.getOrganizationById(id));
  }

  @PostMapping
  public ResponseEntity<OrganizationDTO.Response> createOrganization(
      @Valid @RequestBody OrganizationDTO.Request request,
      @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.createOrganization(request, updatedBy));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
    organizationService.deleteOrganization(id);
    return ResponseEntity.noContent().build();
  }
}
