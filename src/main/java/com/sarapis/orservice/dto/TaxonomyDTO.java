package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaxonomyDTO {
  private String id;
  private String name;
  private String description;
  private String uri;
  private String version;
}
