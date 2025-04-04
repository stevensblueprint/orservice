package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FileImportDTO;

import java.util.HashMap;
import java.util.List;

public interface FileImportService {
    List<FileImportDTO.Response> createFileImports(String exchangeId, HashMap<String, Long> fileSizeMappings, List<String> metadataIds);
}
