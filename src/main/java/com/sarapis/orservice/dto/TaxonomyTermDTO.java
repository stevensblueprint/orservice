package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import lombok.Setter;

public class TaxonomyTermDTO {

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    private String vocabulary;

    private String parentId;

    private String description;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String id;
    private String name;
    private String vocabulary;
    private String parentId;
    private String description;
    private TaxonomyTermDTO.Response parent;
    private TaxonomyDTO.Response taxonomy;
    private List<TaxonomyTermDTO.Response> children;
    private List<MetadataDTO.Response> metadata;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest {
    private String name;
    private String vocabulary;
    private String parentId;
    private String description;
  }
}
