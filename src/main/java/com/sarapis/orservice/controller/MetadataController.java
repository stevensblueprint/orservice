package com.sarapis.orservice.controller;


import com.sarapis.orservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sarapis.orservice.utils.MetadataUtils.*;

@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
@Slf4j
public class MetadataController {
    private final OrganizationService organizationService;
    private final ServiceAtLocationService serviceAtLocationService;
    private final ServiceService serviceService;
    private final TaxonomyService taxonomyService;
    private final TaxonomyTermService taxonomyTermService;

    @PutMapping("/undo/{entityType}/{metadataId}")
    public ResponseEntity<?> undoMetadata(
            @PathVariable String entityType,
            @PathVariable String metadataId,
            @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
    ) {
        return switch(entityType) {
            case ORGANIZATION_RESOURCE_TYPE ->
                    ResponseEntity.ok(organizationService.undoOrganizationMetadata(metadataId, updatedBy));
            case SERVICE_AT_LOCATION_RESOURCE_TYPE ->
                    ResponseEntity.ok(serviceAtLocationService.undoServiceAtLocationMetadata(metadataId, updatedBy));
            case SERVICE_RESOURCE_TYPE ->
                    ResponseEntity.ok(serviceService.undoServiceMetadata(metadataId, updatedBy));
            case TAXONOMY_RESOURCE_TYPE ->
                    ResponseEntity.ok(taxonomyService.undoTaxonomyMetadata(metadataId, updatedBy));
            case TAXONOMY_TERM_RESOURCE_TYPE ->
                    ResponseEntity.ok(taxonomyTermService.undoTaxonomyTermMetadata(metadataId, updatedBy));
            default -> ResponseEntity.badRequest().body(null);
        };
    }
}
