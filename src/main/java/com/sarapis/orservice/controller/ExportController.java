package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.service.DataExchangeService;
import com.sarapis.orservice.utils.DataExchangeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Slf4j
public class ExportController {
  private final DataExchangeService dataExchangeService;

  @PostMapping(produces = "application/zip")
  public ResponseEntity<Void> exportFiles(HttpServletResponse response, @RequestBody DataExchangeDTO.Request request) {
    DataExchangeUtils.configureExportResponse(response);
    int statusCode = dataExchangeService.exportFile(response, request);
    return ResponseEntity.status(statusCode).build();
  }
}
