package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Url;
import com.sarapis.orservice.entity.core.Organization;
import java.util.List;
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
  private List<Url> additionalWebsites;
  private int yearIncorporated;
  private String legalStatus;
  private String uri;
  private Organization parentOrganization;
}
