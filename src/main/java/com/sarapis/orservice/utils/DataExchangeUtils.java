package com.sarapis.orservice.utils;

import jakarta.servlet.http.HttpServletResponse;

public class DataExchangeUtils {
    public static String CSV_FORMAT = "text/csv";

    public static String ORGANIZATION_FILE_NAME = "organizations.csv";
    public static String SERVICE_FILE_NAME = "services.csv";

    public static void configureExportResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"orservice.zip\"");
        response.setContentType("application/zip");
    }
}
