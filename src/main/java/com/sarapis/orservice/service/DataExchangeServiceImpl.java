package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.FileImportDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.mapper.DataExchangeMapper;
import com.sarapis.orservice.model.DataExchange;
import com.sarapis.orservice.model.DataExchangeFormat;
import com.sarapis.orservice.model.DataExchangeType;
import com.sarapis.orservice.repository.DataExchangeRepository;
import com.sarapis.orservice.utils.DataExchangeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExchangeServiceImpl implements DataExchangeService {
  private final DataExchangeRepository dataExchangeRepository;
  private final FileImportService fileImportService;
  private final OrganizationService organizationService;
  private final ServiceService serviceService;
  private final DataExchangeMapper dataExchangeMapper;

  @Override
  @Transactional(readOnly = true)
  public PaginationDTO<DataExchangeDTO.Response> getDataExchangesByUserId(
    String userId,
    LocalDateTime fromDate,
    LocalDateTime toDate,
    Integer page,
    Integer perPage
  ) {
    PageRequest pageable = PageRequest.of(page, perPage);
    Page<DataExchange> exchangePage = dataExchangeRepository.findDataExchanges(userId, fromDate, toDate, pageable);
    Page<DataExchangeDTO.Response> dtoPage = exchangePage.map(dataExchangeMapper::toResponseDTO);
    return PaginationDTO.fromPage(dtoPage);
  }

  @Override
  @Transactional(readOnly = true)
  public DataExchangeDTO.Response getDataExchangeById(String id) {
    DataExchange dataExchange = dataExchangeRepository.findById(id).orElseThrow();
    return dataExchangeMapper.toResponseDTO(dataExchange);
  }

  @Override
  @Transactional
  public DataExchangeDTO.Response createDataExchange(
    DataExchangeType type,
    DataExchangeFormat format,
    boolean success,
    String errorMessage,
    Long size,
    String userId
  ) {
    DataExchange dataExchange = new DataExchange();
    dataExchange.setId(UUID.randomUUID().toString());
    dataExchange.setTimestamp(LocalDateTime.now());
    dataExchange.setType(type);
    dataExchange.setSuccess(success);
    dataExchange.setErrorMessage(errorMessage);
    dataExchange.setFormat(format);
    dataExchange.setSize(size);
    dataExchange.setUserId(userId);
    dataExchange.setFileImports(new ArrayList<>());

    DataExchange savedDataExchange = dataExchangeRepository.save(dataExchange);
    return dataExchangeMapper.toResponseDTO(savedDataExchange);
  }

  @Override
  @Transactional
  public int exportFile(HttpServletResponse response, DataExchangeDTO.Request requestDto) {
    try {
      Map<DataExchangeDTO.ExportFile, Exchangeable> exportMappings = createExportMappings();

      ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

      long bytes = 0;

      for (DataExchangeDTO.ExportFile file : requestDto.getFiles()) {
        switch (requestDto.getFormat()) {
          case CSV:
            bytes += exportMappings.get(file).writeCsv(zipOutputStream);
            break;
          case PDF:
            bytes += exportMappings.get(file).writePdf(zipOutputStream);
            break;
        }
      }

      zipOutputStream.finish();
      zipOutputStream.close();

      createDataExchange(DataExchangeType.EXPORT, requestDto.getFormat(), true, null, bytes,
        requestDto.getUserId());
      return 201;
    } catch (Exception e) {
      createDataExchange(DataExchangeType.EXPORT, requestDto.getFormat(), false, e.getMessage(), 0L,
        requestDto.getUserId());
      return 500;
    }
  }

  @Override
  @Transactional
  public int importFile(String userId, List<MultipartFile> files, String updatedBy) {
    try {
      Map<String, Exchangeable> importMappings = createImportMappings();
      List<String> metadataIds = new ArrayList<>();

      // ORDER MATTERS
      for (MultipartFile file : files) {
        if (!(DataExchangeUtils.CSV_FORMAT.equals(file.getContentType()))) {
          return 400;
        }

        importMappings.get(file.getOriginalFilename()).readCsv(file, updatedBy, metadataIds);
      }

      Map<Integer, FileImportDTO.FileImportData> fileSizeMappings =
        IntStream.range(0, files.size())
          .boxed()
          .collect(Collectors.toMap(
            i -> i,
            i -> {
              MultipartFile file = files.get(i);
              return FileImportDTO.FileImportData.builder()
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .build();
            }
          ));
      Long totalSize = fileSizeMappings.values().stream()
        .map(FileImportDTO.FileImportData::getSize)
        .reduce(0L, Long::sum);
      DataExchangeDTO.Response exchangeResponse = createDataExchange(DataExchangeType.IMPORT, DataExchangeFormat.CSV,
        true, null, totalSize, userId);
      fileImportService.createFileImports(exchangeResponse.getId(), fileSizeMappings, metadataIds);
      return 204;
    } catch (Exception e) {
      createDataExchange(DataExchangeType.IMPORT, DataExchangeFormat.CSV, false, e.getMessage(), null, userId);
      return 500;
    }
  }

  private Map<DataExchangeDTO.ExportFile, Exchangeable> createExportMappings() {
    return Map.ofEntries(
      Map.entry(DataExchangeDTO.ExportFile.Organization, organizationService),
      Map.entry(DataExchangeDTO.ExportFile.Service, serviceService)
    );
  }

  private Map<String, Exchangeable> createImportMappings() {
    return Map.ofEntries(
      Map.entry(DataExchangeUtils.ORGANIZATION_FILE_NAME, organizationService),
      Map.entry(DataExchangeUtils.SERVICE_FILE_NAME, serviceService)
    );
  }
}
