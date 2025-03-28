package com.sarapis.orservice.service;

import com.sarapis.orservice.dto.DataExchangeDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.model.DataExchangeFormat;
import com.sarapis.orservice.model.DataExchangeType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DataExchangeService {
    PaginationDTO<DataExchangeDTO.Response> getDataExchangesByUserId(String userId, Integer page, Integer perPage);
    DataExchangeDTO.Response getDataExchangeById(String id);
    DataExchangeDTO.Response createDataExchange(DataExchangeType type, DataExchangeFormat format, boolean success,
                                                String errorMessage, Long size, String userId);

    int exportFile(HttpServletResponse response, DataExchangeDTO.Request requestDto);
    int importFile(DataExchangeFormat format, String userId, MultipartFile file, String updatedBy);
}
