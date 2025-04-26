package com.sarapis.orservice.utils;

import com.sarapis.orservice.dto.DataExchangeDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class DataExchangeUtils {
    public static String CSV_FORMAT = "text/csv";

    public static Map<String, Integer> IMPORT_ORDER = Map.ofEntries(
      Map.entry(DataExchangeDTO.ExchangeableFile.ORGANIZATION.toFileName(), 0),
      Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE.toFileName(), 1),
      Map.entry(DataExchangeDTO.ExchangeableFile.LOCATION.toFileName(), 1),
      Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE_AT_LOCATION.toFileName(), 2)
    );

    public static void configureExportResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"orservice.zip\"");
        response.setContentType("application/zip");
    }
}
