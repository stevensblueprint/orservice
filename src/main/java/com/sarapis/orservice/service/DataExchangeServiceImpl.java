package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.MetadataDTO;
import com.sarapis.orservice.dto.OrganizationDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExchangeServiceImpl implements DataExchangeService {
    private final DataExchangeRepository dataExchangeRepository;
    private final FileImportService fileImportService;
    private final OrganizationService organizationService;
    private final DataExchangeMapper dataExchangeMapper;

    @Override
    @Transactional(readOnly = true)
    public PaginationDTO<DataExchangeDTO.Response> getDataExchangesByUserId(String userId, Integer page, Integer perPage) {
        PageRequest pageable = PageRequest.of(page, perPage);
        Page<DataExchange> exchangePage;
        if (userId != null) {
            exchangePage = dataExchangeRepository.findByUserId(userId, pageable);
        } else {
            exchangePage = dataExchangeRepository.findAll(pageable);
        }
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
    public DataExchangeDTO.Response createDataExchange(DataExchangeType type, DataExchangeFormat format, boolean success,
                                                       String errorMessage, Long size, String userId) {
        DataExchange dataExchange = new DataExchange();
        dataExchange.setId(UUID.randomUUID().toString());
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
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

            long bytes = 0;

            switch (requestDto.getFormat()) {
                case CSV:
                    bytes += organizationService.writeCsv(zipOutputStream);
                    break;
                case PDF:
                    bytes += organizationService.writePdf(zipOutputStream);
                    break;
            }

            zipOutputStream.finish();
            zipOutputStream.close();

            createDataExchange(DataExchangeType.EXPORT, requestDto.getFormat(), true, null, bytes,
                    requestDto.getUserId());
            return 201;
        } catch (Exception e) {
            createDataExchange(DataExchangeType.EXPORT, requestDto.getFormat(), false, e.getMessage(), null,
                    requestDto.getUserId());
            return 500;
        }
    }

    @Override
    @Transactional
    public int importFile(DataExchangeFormat format, String userId, MultipartFile file, String updatedBy) {
        try {
            List<String> metadataIds = new ArrayList<>();

            switch (format) {
                case CSV: {
                    if (!(DataExchangeUtils.CSV_FORMAT.equals(file.getContentType()))) {
                        return 400;
                    }

                    switch (Objects.requireNonNull(file.getOriginalFilename())) {
                        case "organizations.csv":
                            OrganizationDTO.csvToOrganizations(file.getInputStream()).forEach(createRequest -> {
                                OrganizationDTO.Response response = organizationService.createOrganization(createRequest, updatedBy);
                                metadataIds.addAll(response.getMetadata().stream().map(MetadataDTO.Response::getId).toList());
                            });
                        default:
                            break;
                    }
                }
                case PDF:
                    break;
            }

            DataExchangeDTO.Response exchangeResponse = createDataExchange(DataExchangeType.IMPORT, format, true,
                    null, file.getSize(), userId);
            fileImportService.createFileImport(file.getOriginalFilename(), exchangeResponse.getId(), metadataIds);
            return 204;
        } catch (Exception e) {
            createDataExchange(DataExchangeType.IMPORT, format, false, e.getMessage(), null, userId);
            return 500;
        }
    }
}
