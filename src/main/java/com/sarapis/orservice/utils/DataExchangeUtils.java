package com.sarapis.orservice.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;

public class DataExchangeUtils {
    public static String CSV_FORMAT = "text/csv";

    public static void configureExportResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"orservice.zip\"");
        response.setContentType("application/zip");
    }

//    public static HashMap<String, Runnable> fileImportMappings = new HashMap<>{}
}
