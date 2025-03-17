package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.FileImportDTO;
import com.sarapis.orservice.model.FileImport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileImportMapper {
    FileImportDTO.Response toResponseDTO(FileImport entity);
}
