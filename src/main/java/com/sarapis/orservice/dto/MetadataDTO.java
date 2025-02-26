package com.sarapis.orservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import lombok.Setter;

public class MetadataDTO {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String id;

    @NotNull(message = "Resource ID is required")
    private String resourceId;

    @NotBlank(message = "Resource type is required")
    private String resourceType;

    @NotNull(message = "Last action date is required")
    private LocalDate lastActionDate;

    @NotBlank(message = "Last action type is required")
    private String lastActionType;

    @NotBlank(message = "Field name is required")
    private String fieldName;

    @NotBlank(message = "Previous value is required")
    private String previousValue;

    @NotBlank(message = "Replacement value is required")
    private String replacementValue;

    @NotBlank(message = "Updated by is required")
    private String updatedBy;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String id;
    private String resourceId;
    private String resourceType;
    private LocalDate lastActionDate;
    private String lastActionType;
    private String fieldName;
    private String previousValue;
    private String replacementValue;
    private String updatedBy;
  }
}