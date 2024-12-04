package com.sarapis.orservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
public class ImportController {
  @GetMapping("/csv")
  public void exportCSV() {
  }

  @PostMapping("/csv")
  public void importCSV() {
  }

  @GetMapping("/pdf")
  public void exportPDF() {

  }

  @PostMapping("/pdf")
  public void importPDF() {

  }
}
