package com.sarapis.orservice.controller;

import com.sarapis.orservice.service.DataExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
@Slf4j
public class ImportController {
  private final DataExchangeService dataExchangeService;

  @PostMapping
  public ResponseEntity<Void> importFile(
    @RequestParam("userId") String userId,
    @RequestPart("files") List<MultipartFile> files,
    @CookieValue(value = "updatedBy", required = false, defaultValue = "SYSTEM") String updatedBy
  ) {
    int status = dataExchangeService.importFile(userId, files, updatedBy);
    return ResponseEntity.status(status).build();
  }
}
