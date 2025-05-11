package com.sarapis.orservice.utils;

import com.sarapis.orservice.dto.DataExchangeDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public class DataExchangeUtils {
  public static String CSV_FORMAT = "text/csv";
  public static String CSV_EXTENSION = ".csv";
  public static String PDF_EXTENSION = ".pdf";

  public static Map<String, Integer> IMPORT_ORDER = Map.ofEntries(
    Map.entry(DataExchangeDTO.ExchangeableFile.ORGANIZATION.toFileName(), 0),
    Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE.toFileName(), 1),
    Map.entry(DataExchangeDTO.ExchangeableFile.LOCATION.toFileName(), 3),
    Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE_AT_LOCATION.toFileName(), 2)
  );

  public static void configureExportResponse(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader("Content-Disposition", "attachment; filename=\"orservice.zip\"");
    response.setContentType("application/zip");
  }

  public static String addExtension(String fileName, String extension) {
    return fileName + extension;
  }

  public static String getOriginalFileNameNoExtensions(MultipartFile file) {
    if (file == null) {
      return null;
    }

    String fileName = file.getOriginalFilename();
    if (fileName == null) {
      return null;
    }

    return fileName.substring(0, fileName.lastIndexOf('.'));
  }
}
