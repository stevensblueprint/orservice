package com.sarapis.orservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
  private Long id;
  private String name;
  private String alternateName;
  private String description;
  private String email;
  private String website;
}
