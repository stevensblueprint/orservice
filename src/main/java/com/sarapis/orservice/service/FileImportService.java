package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FileImportDTO;

import java.util.List;
import java.util.Map;

public interface FileImportService {
    List<FileImportDTO.Response> createFileImports(String exchangeId,
                                                   Map<Integer, FileImportDTO.FileImportData> fileSizeMappings,
                                                   List<String> metadataIds);
}
