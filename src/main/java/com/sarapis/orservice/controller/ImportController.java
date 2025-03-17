package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ExchangeDTO;
import com.sarapis.orservice.dto.FileImportDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.model.ExchangeFormat;
import com.sarapis.orservice.model.ExchangeType;
import com.sarapis.orservice.service.ExchangeService;
import com.sarapis.orservice.service.FileImportService;
import com.sarapis.orservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
@Slf4j
public class ImportController {
    private final OrganizationService organizationService;
    private final ExchangeService exchangeService;
    private final FileImportService fileImportService;

    @PostMapping
    public ResponseEntity<Void> importFile(@RequestParam("format") ExchangeFormat format,
                                           @RequestParam("userId") String userId,
                                           @RequestPart("file") MultipartFile file) {
        try {
            List<String> metadataIds = new ArrayList<>();

            switch (format) {
                case CSV: {
                    if (!("text/csv".equals(file.getContentType()))) {
                        return ResponseEntity.badRequest().build();
                    }

                    switch (Objects.requireNonNull(file.getOriginalFilename())) {
                        case "organizations.csv":
                            OrganizationDTO.csvToOrganizations(file.getInputStream()).forEach(createRequest -> {
                                OrganizationDTO.Response response = organizationService.createOrganization(createRequest);
                                metadataIds.addAll(response.getMetadata().stream().map(MetadataDTO.Response::getId).toList());
                            });
                        default:
                            break;
                    }
                }
                case PDF:
                    break;
            }

            ExchangeDTO.Response exchangeResponse = exchangeService.createExchange(ExchangeType.IMPORT, format, true,
                    null, file.getSize(), userId);
            fileImportService.createFileImport(file.getOriginalFilename(), exchangeResponse.getId(), metadataIds);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            exchangeService.createExchange(ExchangeType.IMPORT, format, false, e.getMessage(), null, userId);
            return ResponseEntity.badRequest().build();
        }
    }
}
