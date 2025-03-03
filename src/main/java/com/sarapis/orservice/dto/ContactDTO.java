package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.validator.ValidEmail;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ContactDTO {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;
    private String name;
    private String title;
    private String department;
    @ValidEmail
    private String email;
    private String organizationId;
    private String serviceId;
    private String serviceAtLocationId;
    private String locationId;
  }


  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    private String name;
    private String title;
    private String department;
    private String email;
    @JsonIgnore
    private String organizationId;
    @JsonIgnore
    private String serviceId;
    @JsonIgnore
    private String serviceAtLocationId;
    @JsonIgnore
    private String locationId;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }

}
