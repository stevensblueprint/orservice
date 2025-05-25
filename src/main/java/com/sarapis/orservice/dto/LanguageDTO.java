package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LanguageDTO {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;
    private String serviceId;
    private String locationId;
    private String phoneId;
    private String name;
    private String code;
    private String note;
    private List<AttributeDTO.Request> attributes;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    @JsonIgnore
    private String serviceId;
    @JsonIgnore
    private String locationId;
    @JsonIgnore
    private String phoneId;
    private String name;
    private String code;
    private String note;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
