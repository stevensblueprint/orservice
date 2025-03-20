package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import com.sarapis.orservice.validator.ValidYear;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrganizationDTO {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
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

    @Builder.Default
    private List<UrlDTO.Request> additionalWebsites = new ArrayList<>();

    @Builder.Default
    private List<FundingDTO.Request> funding = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Request> contacts = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Request> phones = new ArrayList<>();

    @Builder.Default
    private List<LocationDTO.Request> locations = new ArrayList<>();

    @Builder.Default
    private List<ProgramDTO.Request> programs = new ArrayList<>();

    @Builder.Default
    private List<ServiceDTO.Request> services = new ArrayList<>();

    @Builder.Default
    private List<OrganizationIdentifierDTO.Request> organizationIdentifiers = new ArrayList<>();
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
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

    @Builder.Default
    private List<UrlDTO.Response> additionalWebsites = new ArrayList<>();

    @Builder.Default
    private List<FundingDTO.Response> funding = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Response> contacts = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Response> phones = new ArrayList<>();

    @Builder.Default
    private List<LocationDTO.Response> locations = new ArrayList<>();

    @Builder.Default
    private List<ProgramDTO.Response> programs = new ArrayList<>();

    @Builder.Default
    private List<OrganizationIdentifierDTO.Response> organizationIdentifiers = new ArrayList<>();

    @Builder.Default
    private List<ServiceDTO.Summary> services = new ArrayList<>();

    @Builder.Default
    private List<AttributeDTO.Response> attributes = new ArrayList<>();

    @Builder.Default
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }
}
