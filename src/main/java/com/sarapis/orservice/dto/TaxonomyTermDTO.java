package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaxonomyTermDTO {
  private String id;
  private String code;
  private String name;
  private TaxonomyTermDTO parent;
  private String taxonomyName;
  private TaxonomyDTO taxonomy;
  private String language;
  private String termUri;
}
