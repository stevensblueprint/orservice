package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.dto.upsert.UpsertOrganizationDTO;
import com.sarapis.orservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<PaginationDTO<OrganizationDTO>> getAllOrganizations(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                              @RequestParam(name = "perPage", defaultValue = "10") int perPage)
    {
        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();

        try {
            PaginationDTO<OrganizationDTO> paginationDTO = PaginationDTO.of(
                    organizations,
                    page,
                    perPage
            );
            return ResponseEntity.ok(paginationDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable String organizationId) {
        OrganizationDTO organization = this.organizationService.getOrganizationById(organizationId);
        return ResponseEntity.ok(organization);
    }

    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody UpsertOrganizationDTO upsertOrganizationDTO) {
        OrganizationDTO createdOrganization = this.organizationService.createOrganization(upsertOrganizationDTO);
        return ResponseEntity.ok(createdOrganization);
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable String organizationId,
                                                              @RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO updatedOrganization = this.organizationService
                .updateOrganization(organizationId, organizationDTO);
        return ResponseEntity.ok(updatedOrganization);
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String organizationId) {
        this.organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }
}
