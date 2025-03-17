package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.FileImportDTO;
import com.sarapis.orservice.mapper.FileImportMapper;
import com.sarapis.orservice.model.FileImport;
import com.sarapis.orservice.model.Metadata;
import com.sarapis.orservice.repository.FileImportRepository;
import com.sarapis.orservice.repository.MetadataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileImportServiceImpl implements FileImportService {
    private final FileImportRepository fileImportRepository;
    private final MetadataRepository metadataRepository;
    private final FileImportMapper fileImportMapper;

    @Override
    @Transactional
    public FileImportDTO.Response createFileImport(String fileName, String exchangeId, List<String> metadataIds) {
        String id = UUID.randomUUID().toString();
        List<Metadata> metadataList = metadataIds.stream().map(metadataId -> {
            Metadata metadata = metadataRepository.findById(metadataId).orElseThrow();
            metadata.setFileImportId(id);
            metadataRepository.save(metadata);
            return metadata;
        }).toList();

        FileImport fileImport = new FileImport();
        fileImport.setId(id);
        fileImport.setTimestamp(LocalDate.now());
        fileImport.setFileName(fileName);
        fileImport.setExchangeId(exchangeId);
        fileImport.setMetadata(metadataList);

        FileImport savedFileImport = fileImportRepository.save(fileImport);
        FileImportDTO.Response response = fileImportMapper.toResponseDTO(savedFileImport);
        return response;
    }
}
