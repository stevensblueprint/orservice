package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.DataExchangeFileDTO;

import java.util.List;

public interface DataExchangeFileService {
    List<DataExchangeFileDTO.Response> addExportedFiles(String dataExchangeId,
                                                        List<DataExchangeFileDTO.ExchangeFileData> files);
    List<DataExchangeFileDTO.Response> addImportedFiles(String dataExchangeId,
                                                        List<DataExchangeFileDTO.ExchangeFileData> files,
                                                        List<String> metadataIds);
}
