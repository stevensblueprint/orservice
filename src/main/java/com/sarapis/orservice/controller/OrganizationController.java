package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
      @RequestParam(name = "search") String search,
      @RequestParam(name = "full_service") Boolean fullService,
      @RequestParam(name = "full") Boolean full,
      @RequestParam(name = "taxonomy_term_id") String taxonomyTermId,
      @RequestParam(name = "taxonomy_id") String taxonomyId,
      @RequestParam(name = "page") Integer page,
      @RequestParam(name = "per_page") Integer perPage,
      @RequestParam(name = "format", defaultValue = "json") String format
  ) {
    return ResponseEntity.ok(organizationService.getAllOrganizations(
        search,
        fullService,
        full,
        taxonomyTermId,
        taxonomyId,
        page,
        perPage,
        format
    ));
  }
}
