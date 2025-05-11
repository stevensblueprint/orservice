package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.DataExchangeFileDTO;
import com.sarapis.orservice.mapper.DataExchangeFileMapper;
import com.sarapis.orservice.model.DataExchange;
import com.sarapis.orservice.model.DataExchangeFile;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.DataExchangeFileRepository;
import com.sarapis.orservice.repository.DataExchangeRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExchangeFileServiceImpl implements DataExchangeFileService {
  private final DataExchangeRepository dataExchangeRepository;
  private final DataExchangeFileRepository dataExchangeFileRepository;
  private final MetadataRepository metadataRepository;
  private final DataExchangeFileMapper dataExchangeFileMapper;

  @Override
  @Transactional
  public List<DataExchangeFileDTO.Response> addExportedFiles(
    String dataExchangeId,
    List<DataExchangeFileDTO.ExchangeFileData> files
  ) {
    List<DataExchangeFile> exchangeFiles = files.stream().map(file -> {
      String id = UUID.randomUUID().toString();

      DataExchangeFile dataExchangeFile = new DataExchangeFile();
      dataExchangeFile.setId(id);
      dataExchangeFile.setFileName(file.getFileName());
      dataExchangeFile.setSize(file.getSize());
      dataExchangeFile.setDataExchangeId(dataExchangeId);

      return dataExchangeFileRepository.save(dataExchangeFile);
    }).toList();

    DataExchange dataExchange = dataExchangeRepository.findById(dataExchangeId).orElseThrow();
    dataExchange.getDataExchangeFiles().addAll(exchangeFiles);
    dataExchangeRepository.save(dataExchange);

    return exchangeFiles.stream().map(dataExchangeFileMapper::toResponseDTO).toList();
  }

  @Override
  @Transactional
  public List<DataExchangeFileDTO.Response> addImportedFiles(
    String dataExchangeId,
    List<DataExchangeFileDTO.ExchangeFileData> files,
    List<String> metadataIds
  ) {
    List<DataExchangeFile> exchangeFiles = files.stream().map(file -> {
      String id = UUID.randomUUID().toString();

      List<Metadata> metadataList = metadataIds.stream().map(metadataId -> {
        Metadata metadata = metadataRepository.findById(metadataId).orElseThrow();
        metadata.setDataExchangeFileId(id);
        metadataRepository.save(metadata);
        return metadata;
      }).toList();

      DataExchangeFile dataExchangeFile = new DataExchangeFile();
      dataExchangeFile.setId(id);
      dataExchangeFile.setFileName(file.getFileName());
      dataExchangeFile.setSize(file.getSize());
      dataExchangeFile.setMetadata(metadataList);

      return dataExchangeFileRepository.save(dataExchangeFile);
    }).toList();

    DataExchange dataExchange = dataExchangeRepository.findById(dataExchangeId).orElseThrow();
    dataExchange.getDataExchangeFiles().addAll(exchangeFiles);
    dataExchangeRepository.save(dataExchange);

    return exchangeFiles.stream().map(dataExchangeFileMapper::toResponseDTO).toList();
  }
}
