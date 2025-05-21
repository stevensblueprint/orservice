package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ServiceCapacityDTO {
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class Request {
    private String id;
    @NotNull
    private UnitDTO.Request unit;
    @NotNull
    private int available;
    @Min(0)
    private int number;
    private int maximum;
    private String description;
    private LocalDate updated;
    private String serviceId;
  }
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class Response {
    private String id;
    @JsonIgnore
    private String serviceId;
    private UnitDTO.Response unit;
    private int available;
    private int maximum;
    private String description;
    private LocalDate updated;
    @Builder.Default
    private List<AttributeDTO.Response> attributes = new ArrayList<>();
    @Builder.Default
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }
}
