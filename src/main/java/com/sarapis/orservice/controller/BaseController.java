package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.RootDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BaseController {
  @GetMapping
  public ResponseEntity<RootDTO> getRoot() {
    RootDTO root = RootDTO.builder()
        .version("3.0")
        .profile("https://docs.openreferraluk.org/en/latest/")
        .openapiUrl("https://raw.githubusercontent.com/openreferral/specification/3.0/schema/openapi.json")
        .build();
    return ResponseEntity.ok(root);
  }
}