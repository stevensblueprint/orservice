package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.DataExchangeFileDTO;
import com.sarapis.orservice.model.DataExchangeFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataExchangeFileMapper {
    DataExchangeFileDTO.Response toResponseDTO(DataExchangeFile entity);
}
