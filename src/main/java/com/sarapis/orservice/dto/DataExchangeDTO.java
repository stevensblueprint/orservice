package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.DataExchangeFormat;
import com.sarapis.orservice.model.DataExchangeType;
import lombok.*;

import java.util.List;

public class DataExchangeDTO {
    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private DataExchangeFormat format;
        private String userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String id;
        private DataExchangeType type;
        private Boolean success;
        private String errorMessage;
        private DataExchangeFormat format;
        private Integer size;
        private String userId;
        private List<FileImportDTO.Response> fileImports;
    }
}
