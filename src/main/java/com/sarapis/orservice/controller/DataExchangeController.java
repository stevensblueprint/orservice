package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.DataExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
@Slf4j
public class DataExchangeController {
    private final DataExchangeService dataExchangeService;

    @GetMapping
    public ResponseEntity<PaginationDTO<DataExchangeDTO.Response>> getAllExchanges(
            @RequestParam(name = "user_id") String userId,
            @RequestParam(name = "from_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,
            @RequestParam(name = "to_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer perPage
    ) {
        PaginationDTO<DataExchangeDTO.Response> exchanges = dataExchangeService
                .getDataExchangesByUserId(userId, fromDate, toDate, page, perPage);
        return ResponseEntity.ok(exchanges);
    }
}
