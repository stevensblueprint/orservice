package com.sarapis.orservice.controller;

import com.sarapis.orservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    private final OrganizationService organizationService;

    @Autowired
    public ExportController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping("/csv")
    public ResponseEntity<Resource> exportCSV() {
        String filename = "organizations.csv";
        InputStreamResource organizationCSV = new InputStreamResource(organizationService.loadCSV());
        return ResponseEntity.ok().body(organizationCSV);
    }
}
