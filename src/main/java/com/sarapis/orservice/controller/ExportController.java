package com.sarapis.orservice.controller;

import com.amazonaws.util.IOUtils;
import com.sarapis.orservice.service.OrganizationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    private final OrganizationService organizationService;

    @Autowired
    public ExportController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(value = "/csv", produces = "application/zip")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"orservice\"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        ArrayList<String> files = new ArrayList<>();
        files.add("organizations.csv");

        for (String file : files) {
            switch (file) {
                case "organizations.csv": {
                    zipOutputStream.putNextEntry(new ZipEntry(file));
                    IOUtils.copy(organizationService.loadCSV(), zipOutputStream);
                    zipOutputStream.closeEntry();
                }
                case "services.csv":
                case "locations.csv":
                case "service_at_locations.csv":
                default:
            }
        }

        zipOutputStream.finish();
        zipOutputStream.close();
    }
}
