package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FileImportDTO;

import java.util.List;

public interface FileImportService {
    FileImportDTO.Response createFileImport(String fileName, String exchangeId, List<String> metadataIds);
}
