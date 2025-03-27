package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ExchangeDTO;
import com.sarapis.orservice.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
@Slf4j
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping()
    public ResponseEntity<List<ExchangeDTO.Response>> getAllExchanges(
            @RequestParam(name = "userId", required = false) String userId
    ) {
        List<ExchangeDTO.Response> exchanges = exchangeService.getExchangesByUserId(userId);
        return ResponseEntity.ok(exchanges);
    }
}
