package com.sarapis.orservice.controller;

import com.sarapis.orservice.service.OrganizationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/export")
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

        organizationService.writeCsv(zipOutputStream);

        zipOutputStream.finish();
        zipOutputStream.close();
    }

    @GetMapping(value = "/pdf", produces = "application/zip")
    public void exportPdf(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"orservice\"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        organizationService.writePdf(zipOutputStream);

        zipOutputStream.finish();
        zipOutputStream.close();
    }
}
