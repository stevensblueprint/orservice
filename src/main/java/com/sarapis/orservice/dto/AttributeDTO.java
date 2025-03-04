package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.Setter;

public class AttributeDTO {

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;

    private String linkId;

    @NotNull(message = "Taxonomy Term is Required")
    private String taxonomyTermId;

    private String linkType;

    @NotNull(message = "Link entity is required")
    private String linkEntity;

    private String value;

    private String label;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    private String linkId;
    private String taxonomyTermId;
    private String linkType;
    private String linkEntity;
    private String value;
    private String label;
    private TaxonomyTermDTO.Response taxonomyTerm;
    private List<MetadataDTO.Response> metadata;
  }
}
