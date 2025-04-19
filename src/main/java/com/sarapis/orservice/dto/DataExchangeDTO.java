package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.DataExchangeFormat;
import com.sarapis.orservice.model.DataExchangeType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataExchangeDTO {
  public enum ExchangeableFile {
    organization ("organizations.csv"),
    service ("services.csv");

    private final String fileName;

    ExchangeableFile(String fileName) {
      this.fileName = fileName;
    }

    public String toFileName() {
      return this.fileName;
    }
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
    private List<ExchangeableFile> files;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    private LocalDateTime timestamp;
    private DataExchangeType type;
    private Boolean success;
    private String errorMessage;
    private DataExchangeFormat format;
    private Long size;
    private String userId;

    @Builder.Default
    private List<DataExchangeFileDTO.Response> dataExchangeFiles = new ArrayList<>();
  }
}
