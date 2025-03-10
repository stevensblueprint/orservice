package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import com.sarapis.orservice.validator.ValidYear;
import java.util.List;
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
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    private String parentOrganizationId;
    private List<UrlDTO.Request> additionalWebsites;
    private List<FundingDTO.Request> funding;
    private List<ContactDTO.Request> contacts;
    private List<PhoneDTO.Request> phones;
    private List<LocationDTO.Request> locations;
    private List<ProgramDTO.Request> programs;
    private List<OrganizationIdentifierDTO.Request> organizationIdentifiers;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    private String parentOrganizationId;
    private List<UrlDTO.Response> additionalWebsites;
    private List<FundingDTO.Response> funding;
    private List<ContactDTO.Response> contacts;
    private List<PhoneDTO.Response> phones;
    private List<LocationDTO.Response> locations;
    private List<ProgramDTO.Response> programs;
    private List<OrganizationIdentifierDTO.Response> organizationIdentifiers;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
