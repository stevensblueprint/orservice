package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.ExchangeDTO;
import com.sarapis.orservice.mapper.ExchangeMapper;
import com.sarapis.orservice.model.Exchange;
import com.sarapis.orservice.model.ExchangeFormat;
import com.sarapis.orservice.model.ExchangeType;
import com.sarapis.orservice.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final ExchangeMapper exchangeMapper;

    @Override
    @Transactional(readOnly = true)
    public ExchangeDTO.Response getExchangeById(String id) {
        Exchange exchange = exchangeRepository.findById(id).orElseThrow();
        ExchangeDTO.Response response = exchangeMapper.toResponseDTO(exchange);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeDTO.Response> getExchangesByUserId(String userId) {
        List<Exchange> exchanges = exchangeRepository.findByUserId(userId);
        List<ExchangeDTO.Response> response = exchanges.stream().map(exchangeMapper::toResponseDTO).toList();
        return response;
    }

    @Override
    @Transactional
    public ExchangeDTO.Response createExchange(ExchangeType type, ExchangeFormat format, boolean success,
                                               String errorMessage, Long size, String userId) {
        Exchange exchange = new Exchange();
        exchange.setId(UUID.randomUUID().toString());
        exchange.setType(type);
        exchange.setSuccess(success);
        exchange.setErrorMessage(errorMessage);
        exchange.setFormat(format);
        exchange.setSize(size);
        exchange.setUserId(userId);
        exchange.setFileImports(new ArrayList<>());

        Exchange savedExchange = exchangeRepository.save(exchange);
        ExchangeDTO.Response response = exchangeMapper.toResponseDTO(savedExchange);
        return response;
    }
}
