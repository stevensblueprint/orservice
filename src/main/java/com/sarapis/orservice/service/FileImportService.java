package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FileImportDTO;

import java.util.HashMap;
import java.util.List;

public interface FileImportService {
    List<FileImportDTO.Response> createFileImports(String exchangeId,
                                                   HashMap<Integer, FileImportDTO.FileImportData> fileSizeMappings,
                                                   List<String> metadataIds);
}
