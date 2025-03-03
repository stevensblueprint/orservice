package com.sarapis.orservice.dto;

import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import com.sarapis.orservice.validator.ValidYear;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrganizationDTO {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String id;
    @NotBlank
    private String name;
    private String alternateName;
    @NotBlank
    private String description;
    @ValidEmail
    private String email;
    @ValidUrl
    private String website;
    private String taxStatus;
    private String taxId;

    @ValidYear
    private Integer yearIncorporated;

    private String legalStatus;

    @ValidUrl
    private String logo;

    @ValidUrl
    private String uri;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String id;
    private String name;
    private String alternateName;
    private String description;
    private String email;
    private String website;
    private String taxStatus;
    private String taxId;
    private Integer yearIncorporated;
    private String legalStatus;
    private String logo;
    private String uri;
  }
}
