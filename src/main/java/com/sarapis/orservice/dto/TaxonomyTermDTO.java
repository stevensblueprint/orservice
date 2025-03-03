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
    @NotBlank(message = "Code is required")
    private String code;
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
    private String code;
    private String name;
    private String description;
    private String parentId;
    private TaxonomyTermDTO.Response parent;
    private TaxonomyTermDTO.Response children;
    private TaxonomyDTO.Response taxonomy;
    private String language;
    private String termUri;
    private List<MetadataDTO.Response> metadata;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest {
    private String code;
    private String name;
    private String vocabulary;
    private String description;
    private String parentId;
    private List<String> childrenId;
    private String taxonomyId;
    private String language;
    private String termUri;
  }
}
