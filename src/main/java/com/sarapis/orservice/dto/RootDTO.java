package com.sarapis.orservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RootDTO {
  private String version;
  private String profile;
  private String openapiUrl;
}