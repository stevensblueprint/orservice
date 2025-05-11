package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.DataExchangeFileDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.exceptions.ResourceNotFoundException;
import com.sarapis.orservice.mapper.DataExchangeMapper;
import com.sarapis.orservice.mapper.MetadataMapper;
import com.sarapis.orservice.model.DataExchange;
import com.sarapis.orservice.model.DataExchangeFormat;
import com.sarapis.orservice.model.DataExchangeType;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.DataExchangeRepository;
import com.sarapis.orservice.utils.DataExchangeUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.zip.ZipOutputStream;

import static com.sarapis.orservice.utils.MetadataUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExchangeServiceImpl implements DataExchangeService {
  private final DataExchangeMapper dataExchangeMapper;
  private final DataExchangeFileService dataExchangeFileService;
  private final OrganizationService organizationService;
  private final ServiceService serviceService;
  private final LocationService locationService;
  private final ServiceAtLocationService serviceAtLocationService;
  private final DataExchangeRepository dataExchangeRepository;
  private final TransactionTemplate transactionTemplate;
  private final MetadataService metadataService;
  private final MetadataMapper metadataMapper;

  private Map<String, BiConsumer<List<Metadata>, String>> undoBatchTypeMap;

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
    dataExchange.setTimestamp(LocalDateTime.now());
    dataExchange.setType(type);
    dataExchange.setSuccess(success);
    dataExchange.setErrorMessage(errorMessage);
    dataExchange.setFormat(format);
    dataExchange.setSize(size);
    dataExchange.setUserId(userId);
    dataExchange.setDataExchangeFiles(new ArrayList<>());

    DataExchange savedDataExchange = dataExchangeRepository.save(dataExchange);
    return dataExchangeMapper.toResponseDTO(savedDataExchange);
  }

  @Override
  @Transactional
  public int exportFile(HttpServletResponse response, DataExchangeDTO.Request requestDto) {
    try {
      Map<String, Exchangeable> exportMappings = createExportMappings();

      ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

      List<DataExchangeFileDTO.ExchangeFileData> fileDataList = new ArrayList<>();
      for (DataExchangeDTO.ExchangeableFile file : requestDto.getFiles()) {
        switch (requestDto.getFormat()) {
          case CSV:
            fileDataList.add(DataExchangeFileDTO.ExchangeFileData.builder()
              .fileName(DataExchangeUtils.addExtension(file.toFileName(), DataExchangeUtils.CSV_EXTENSION))
              .size(exportMappings.get(file.name()).writeCsv(zipOutputStream))
              .build());
            break;
          case PDF:
            fileDataList.add(DataExchangeFileDTO.ExchangeFileData.builder()
              .fileName(DataExchangeUtils.addExtension(file.toFileName(), DataExchangeUtils.PDF_EXTENSION))
              .size(exportMappings.get(file.name()).writePdf(zipOutputStream))
              .build());
            break;
          default:
            throw new Exception("File type not supported.");
        }
      }

      zipOutputStream.finish();
      zipOutputStream.close();

      long totalBytes = fileDataList.stream().mapToLong(DataExchangeFileDTO.ExchangeFileData::getSize).sum();
      DataExchangeDTO.Response exchangeResponse = createDataExchange(DataExchangeType.EXPORT, requestDto.getFormat(),
        true, null, totalBytes, requestDto.getUserId());
      dataExchangeFileService.addExportedFiles(exchangeResponse.getId(), fileDataList);
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
      // Creates a new transaction per execute call
      transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      Map<String, Exchangeable> importMappings = createImportMappings();
      List<String> metadataIds = new ArrayList<>();

      // Transaction for importing data from CSV
      DataExchangeDTO.ImportTransactionResponse importTransaction = transactionTemplate.execute(status -> {
        files.sort(Comparator.comparingInt(file -> DataExchangeUtils.IMPORT_ORDER
          .get(DataExchangeUtils.getOriginalFileNameNoExtensions(file))));
        for (MultipartFile file : files) {
          if (!(DataExchangeUtils.CSV_FORMAT.equals(file.getContentType()))) {
            return DataExchangeDTO.ImportTransactionResponse.builder()
              .statusCode(400)
              .errorMessage("Expected CSV file.")
              .build();
          }

          try {
            importMappings
              .get(DataExchangeUtils.getOriginalFileNameNoExtensions(file))
              .readCsv(file, updatedBy, metadataIds);
          } catch (IOException e) {
            return DataExchangeDTO.ImportTransactionResponse.builder()
              .statusCode(500)
              .errorMessage(e.getMessage())
              .build();
          }
        }
        return DataExchangeDTO.ImportTransactionResponse.builder()
          .statusCode(204)
          .build();
      });

      int statusCode = Objects.requireNonNull(importTransaction).getStatusCode();
      if (statusCode > 200) {
        // Transaction for writing error
        transactionTemplate.execute(status -> {
          createDataExchange(DataExchangeType.IMPORT, DataExchangeFormat.CSV, false,
            importTransaction.getErrorMessage(), 0L, userId);
          return null;
        });
      } else {
        // Transaction for writing success
        transactionTemplate.execute(status -> {
          List<DataExchangeFileDTO.ExchangeFileData> fileDataList = files
            .stream().map(file ->
              DataExchangeFileDTO.ExchangeFileData.builder()
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .build())
            .toList();

          long totalBytes = fileDataList.stream().mapToLong(DataExchangeFileDTO.ExchangeFileData::getSize).sum();
          DataExchangeDTO.Response exchangeResponse = createDataExchange(DataExchangeType.IMPORT, DataExchangeFormat.CSV,
            true, null, totalBytes, userId);
          dataExchangeFileService.addImportedFiles(exchangeResponse.getId(), fileDataList, metadataIds);
          return null;
        });
      }

      return statusCode;
    } catch (Exception e) {
      // Transaction for writing error
      transactionTemplate.execute(status -> {
        createDataExchange(DataExchangeType.IMPORT, DataExchangeFormat.CSV, false, e.getMessage(), 0L, userId);
        return null;
      });
      return 500;
    }
  }

    @Override
    @Transactional
    public int undoImportedFile(String dataExchangeFileId, String resourceType, String updatedBy) {
        List<Metadata> fileMetadata = metadataService.getMetadataByDataExchangeFileIdAndResourceType(dataExchangeFileId, resourceType)
                .stream()
                .map(metadataMapper::toEntity)
                .toList();

        if(fileMetadata.isEmpty()) {
            throw new ResourceNotFoundException(
                String.format("No %s Metadata with dataExchangeFileId %s exists",
                              resourceType,
                              dataExchangeFileId));
        }

        Map<String, BiConsumer<List<Metadata>, String>> undoBatchTypeMap = this.getUndoBatchTypeMap();
        try {
            BiConsumer<List<Metadata>, String> undoBatch = undoBatchTypeMap.get(resourceType);
            undoBatch.accept(fileMetadata, updatedBy);
            return 200;
        } catch (NullPointerException e) {
            log.error("No undo file import applicable for {}", resourceType);
            return 500;
        }
    }

  private Map<String, Exchangeable> createExportMappings() {
    return Map.ofEntries(
      Map.entry(DataExchangeDTO.ExchangeableFile.ORGANIZATION.name(), organizationService),
      Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE.name(), serviceService),
      Map.entry(DataExchangeDTO.ExchangeableFile.LOCATION.name(), locationService),
      Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE_AT_LOCATION.name(), serviceAtLocationService)
    );
  }

  private Map<String, Exchangeable> createImportMappings() {
    return Map.ofEntries(
      Map.entry(DataExchangeDTO.ExchangeableFile.ORGANIZATION.toFileName(), organizationService),
      Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE.toFileName(), serviceService),
      Map.entry(DataExchangeDTO.ExchangeableFile.LOCATION.toFileName(), locationService),
      Map.entry(DataExchangeDTO.ExchangeableFile.SERVICE_AT_LOCATION.toFileName(), serviceAtLocationService)
    );
  }

    private Map<String, BiConsumer<List<Metadata>, String>> getUndoBatchTypeMap() {
        if(this.undoBatchTypeMap == null) {
            this.undoBatchTypeMap = Map.of(
                ORGANIZATION_RESOURCE_TYPE, organizationService::undoOrganizationMetadataBatch,
                SERVICE_RESOURCE_TYPE, serviceService::undoServiceMetadataBatch,
                LOCATION_RESOURCE_TYPE, locationService::undoLocationMetadataBatch,
                SERVICE_AT_LOCATION_RESOURCE_TYPE, serviceAtLocationService::undoServiceAtLocationMetadataBatch
            );
        }
        return this.undoBatchTypeMap;
    }
}
