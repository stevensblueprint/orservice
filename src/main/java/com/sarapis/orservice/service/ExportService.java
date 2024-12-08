package com.sarapis.orservice.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.InputStreamResource;

public interface ExportService {
  InputStreamResource createCsvZip() throws IOException;

  InputStreamResource createPdfZip() throws IOException;
}
