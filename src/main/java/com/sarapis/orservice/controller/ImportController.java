package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/import")
public class ImportController {
    private final OrganizationService organizationService;

    @Autowired
    public ImportController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/csv")
    public ResponseEntity<Void> importCSV(@RequestParam("file") MultipartFile file) throws IOException {
        if ("text/csv".equals(file.getContentType())) {
            switch (Objects.requireNonNull(file.getOriginalFilename())) {
                case "organizations.csv":
                    OrganizationDTO.csvToOrganizations(file.getInputStream()).forEach(organizationService::createOrganization);
                    return ResponseEntity.noContent().build();
                default:
                    break;
            }
        }
        return ResponseEntity.badRequest().body(null);
    }
}
