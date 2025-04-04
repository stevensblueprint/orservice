package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.DataExchangeFormat;
import com.sarapis.orservice.model.DataExchangeType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class DataExchangeDTO {
    public enum ExportFile {
        Organization,
        Service,
        Location,
        ServiceAtLocation
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private DataExchangeFormat format;
        private String userId;
        private List<ExportFile> files;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String id;
        private LocalDateTime timestamp;
        private DataExchangeType type;
        private Boolean success;
        private String errorMessage;
        private DataExchangeFormat format;
        private Integer size;
        private String userId;
        private List<FileImportDTO.Response> fileImports;
    }
}
