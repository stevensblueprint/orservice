package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.ExchangeFormat;
import com.sarapis.orservice.model.ExchangeType;
import lombok.*;

public class ExchangeDTO {
    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private ExchangeFormat format;
        private String userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String id;
        private ExchangeType type;
        private Boolean success;
        private String errorMessage;
        private ExchangeFormat format;
        private Integer size;
        private String userId;
    }
}
