package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ExchangeDTO;
import com.sarapis.orservice.model.ExchangeType;
import com.sarapis.orservice.service.ExchangeService;
import com.sarapis.orservice.service.OrganizationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Slf4j
public class ExportController {
    private final OrganizationService organizationService;
    private final ExchangeService exchangeService;

    @PostMapping(produces = "application/zip")
    public void exportFiles(HttpServletResponse response, @RequestBody ExchangeDTO.Request request) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"orservice.zip\"");
        response.setContentType("application/zip");

        try {
            long bytes = 0;
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

            switch (request.getFormat()) {
                case CSV:
                    bytes += organizationService.writeCsv(zipOutputStream);
                    break;
                case PDF:
                    bytes += organizationService.writePdf(zipOutputStream);
                    break;
            }

            zipOutputStream.finish();
            zipOutputStream.close();

            exchangeService.createExchange(ExchangeType.EXPORT, request.getFormat(), true, null,
                    bytes, request.getUserId());
        } catch (Exception e) {
            exchangeService.createExchange(ExchangeType.EXPORT, request.getFormat(), false, e.getMessage(),
                    null, request.getUserId());
        }
    }
}
