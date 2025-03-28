package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.service.DataExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
@Slf4j
public class DataExchangeController {
    private final DataExchangeService dataExchangeService;

    @GetMapping()
    public ResponseEntity<PaginationDTO<DataExchangeDTO.Response>> getAllExchanges(
            @RequestParam(name = "userId", required = false) String userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer perPage
    ) {
        PaginationDTO<DataExchangeDTO.Response> exchanges = dataExchangeService.getDataExchangesByUserId(userId, page, perPage);
        return ResponseEntity.ok(exchanges);
    }
}
