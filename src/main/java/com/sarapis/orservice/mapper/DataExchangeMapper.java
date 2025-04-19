package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.model.DataExchange;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataExchangeMapper {
    DataExchangeDTO.Response toResponseDTO(DataExchange entity);
}
