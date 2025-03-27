package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ExchangeDTO;
import com.sarapis.orservice.model.ExchangeFormat;
import com.sarapis.orservice.model.ExchangeType;

import java.util.List;

public interface ExchangeService {
    ExchangeDTO.Response getExchangeById(String id);
    List<ExchangeDTO.Response> getExchangesByUserId(String userId);
    ExchangeDTO.Response createExchange(ExchangeType type, ExchangeFormat format, boolean success,
                                        String errorMessage, Long size, String userId);
}
