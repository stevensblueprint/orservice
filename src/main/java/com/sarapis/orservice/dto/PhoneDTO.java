package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.dto.AttributeDTO.Response;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PhoneDTO {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;
    private String locationId;
    private String serviceId;
    private String organizationId;
    private String contactId;
    private String serviceAtLocationId;
    @NotBlank
    private String number;
    private int extension;
    private String type;
    private String description;
    private List<LanguageDTO.Request> languages;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    @JsonIgnore
    private String locationId;
    @JsonIgnore
    private String serviceId;
    @JsonIgnore
    private String organizationId;
    @JsonIgnore
    private String contactId;
    @JsonIgnore
    private String serviceAtLocationId;
    private String number;
    private int extension;
    private String type;
    private String description;
    private List<LanguageDTO.Response> languages;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
