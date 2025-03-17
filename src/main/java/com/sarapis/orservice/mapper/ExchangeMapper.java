package com.sarapis.orservice.mapper;

import com.sarapis.orservice.dto.ExchangeDTO;
import com.sarapis.orservice.model.Exchange;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {
    ExchangeDTO.Response toResponseDTO(Exchange entity);
}
