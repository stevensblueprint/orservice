package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.exception.InvalidInputException;
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

        if(page <= 0) throw new InvalidInputException("Invalid input provided for 'page'.");
        if(perPage <= 0) throw new InvalidInputException("Invalid input provided for 'perPage'.");

        PaginationDTO<OrganizationDTO> paginationDTO = PaginationDTO.of(
            organizations,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable String id) {
        OrganizationDTO organizationDTO = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organizationDTO);
    }

    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO created = organizationService.createOrganization(organizationDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable String id,
        @RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO updated = organizationService.updateOrganization(id, organizationDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}
