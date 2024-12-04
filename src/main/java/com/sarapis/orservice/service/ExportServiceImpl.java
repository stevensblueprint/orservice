package com.sarapis.orservice.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.core.io.InputStreamResource;

public class ExportServiceImpl implements ExportService {
  private static void addToZip(
      ZipOutputStream zipOut,
      String fileName,
      InputStreamResource resource) throws IOException {

    ZipEntry zipEntry = new ZipEntry(fileName);
    zipOut.putNextEntry(zipEntry);

    try (InputStream inputStream = resource.getInputStream()) {
      byte[] bytes = new byte[1024];
      int length;
      while ((length = inputStream.read(bytes)) >= 0) {
        zipOut.write(bytes, 0, length);
      }
    }
    zipOut.closeEntry();
  }

  private Map<String, InputStreamResource> getCSVsFromTables() {
    return null;
  }

  @Override
  public InputStreamResource createCsvZip() throws IOException {
    Map<String, InputStreamResource> files = getCSVsFromTables();
    ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
    try (ZipOutputStream zipOut = new ZipOutputStream(zipStream)) {
      for (Map.Entry<String, InputStreamResource> entry : files.entrySet()) {
        addToZip(zipOut, entry.getKey(), entry.getValue());
      }
    }
    return new InputStreamResource(new ByteArrayInputStream(zipStream.toByteArray()));
  }

  @Override
  public InputStreamResource createPdfZip() throws IOException {
    return null;
  }
}
