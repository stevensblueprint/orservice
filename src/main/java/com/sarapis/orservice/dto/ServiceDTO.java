package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ServiceDTO {
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
    private String description;

    @ValidUrl
    private String url;

    @ValidEmail
    private String email;

    @NotBlank
    private String status;
    private String interpretationServices;
    private String applicationProcess;
    private String feesDescription;
    private String waitTime;
    private String fees;
    private String accreditations;
    private String eligibilityDescription;

    @Min(0)
    @Max(99)
    private Integer minimumAge;

    @Min(0)
    @Max(99)
    private Integer maximumAge;

    private String assuredDate;

    @ValidEmail
    private String assurerEmail;

    private String licenses;
    private String alert;
    private LocalDate lastModified;
    private List<PhoneDTO.Request> phones;
    private List<ScheduleDTO.Request> schedules;
    private List<ServiceAreaDTO.Request> serviceAreas;
    private List<LanguageDTO.Request> languages;
    private OrganizationDTO.Request organization;
    private List<FundingDTO.Request> funding;
    private List<CostOptionDTO.Request> costOptions;
    private ProgramDTO.Request program;
    private List<RequiredDocumentDTO.Request> requiredDocuments;
    private List<ServiceAtLocationDTO.Request> serviceAtLocations;
    private List<ContactDTO.Request> contacts;
    private List<UrlDTO.Request> additionalUrls;
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
    private String url;
    private String email;
    private String status;
    private String interpretationServices;
    private String applicationProcess;
    private String feesDescription;
    private String waitTime;
    private String fees;
    private String accreditations;
    private String eligibilityDescription;
    private Integer minimumAge;
    private Integer maximumAge;
    private String assuredDate;
    private String assurerEmail;
    private String licenses;
    private String alert;
    private LocalDate lastModified;
    private List<PhoneDTO.Response> phones;
    private List<ScheduleDTO.Response> schedules;
    private List<ServiceAreaDTO.Response> serviceAreas;
    private List<LanguageDTO.Response> languages;
    private OrganizationDTO.Response organization;
    private List<FundingDTO.Response> funding;
    private List<CostOptionDTO.Response> costOptions;
    private ProgramDTO.Response program;
    private List<RequiredDocumentDTO.Response> requiredDocuments;
    private List<ServiceAtLocationDTO.Response> serviceAtLocations;
    private List<ContactDTO.Response> contacts;
    private List<UrlDTO.Response> additionalUrls;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
