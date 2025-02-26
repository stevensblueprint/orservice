package com.sarapis.orservice.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.Setter;

public class UrlDTO {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String id;
    private String label;
    @NotBlank
    private String url;
    private String organizationId;
    private String serviceId;
    private List<AttributeDTO.Request> attributes;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String id;
    private String label;
    private String url;
    private String organizationId;
    private String serviceId;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest {
    private String label;
    private String url;
    private String organizationId;
    private String serviceId;
    private List<AttributeDTO.Request> attributes;
  }
}