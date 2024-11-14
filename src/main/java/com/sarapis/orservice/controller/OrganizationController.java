package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.OrganizationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

  private final OrganizationService organizationService;

  @Autowired
  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @GetMapping
  public ResponseEntity<PaginationDTO<OrganizationDTO>> getAllOrganizations() {
    List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
    PaginationDTO<OrganizationDTO> paginationDTO = PaginationDTO.of(
        organizations.size(),
        1,
        10,
        organizations.size(),
        false,
        false,
        false,
        organizations
    );
    return ResponseEntity.ok(paginationDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable String id) {
    OrganizationDTO organization = organizationService.getOrganizationById(id);
    return ResponseEntity.ok(organization);
  }

  @PostMapping
  public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
    OrganizationDTO createdOrganization = organizationService.createOrganization(organizationDTO);
    return ResponseEntity.ok(createdOrganization);
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable String id, @RequestBody OrganizationDTO organizationDTO) {
    OrganizationDTO updatedOrganization = organizationService.updateOrganization(id, organizationDTO);
    return ResponseEntity.ok(updatedOrganization);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
    organizationService.deleteOrganization(id);
    return ResponseEntity.noContent().build();
  }

}
