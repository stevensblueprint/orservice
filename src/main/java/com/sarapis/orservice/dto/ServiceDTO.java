package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ServiceDTO {
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

    @Builder.Default
    private List<PhoneDTO.Request> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Request> schedules = new ArrayList<>();

    @Builder.Default
    private List<ServiceAreaDTO.Request> serviceAreas = new ArrayList<>();

    @Builder.Default
    private List<LanguageDTO.Request> languages = new ArrayList<>();

    private OrganizationDTO.Request organization;

    @Builder.Default
    private List<FundingDTO.Request> funding = new ArrayList<>();

    @Builder.Default
    private List<CostOptionDTO.Request> costOptions = new ArrayList<>();

    private ProgramDTO.Request program;

    @Builder.Default
    private List<RequiredDocumentDTO.Request> requiredDocuments = new ArrayList<>();

    @Builder.Default
    private List<ServiceAtLocationDTO.Request> serviceAtLocations = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Request> contacts = new ArrayList<>();

    @Builder.Default
    private List<UrlDTO.Request> additionalUrls = new ArrayList<>();
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

    @Builder.Default
    private List<PhoneDTO.Response> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Response> schedules = new ArrayList<>();

    @Builder.Default
    private List<ServiceAreaDTO.Response> serviceAreas = new ArrayList<>();

    @Builder.Default
    private List<LanguageDTO.Response> languages = new ArrayList<>();
    private OrganizationDTO.Response organization;

    @Builder.Default
    private List<FundingDTO.Response> funding = new ArrayList<>();

    @Builder.Default
    private List<CostOptionDTO.Response> costOptions = new ArrayList<>();
    private ProgramDTO.Response program;

    @Builder.Default
    private List<RequiredDocumentDTO.Response> requiredDocuments = new ArrayList<>();

    @Builder.Default
    private List<ServiceAtLocationDTO.Response> serviceAtLocations = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Response> contacts = new ArrayList<>();

    @Builder.Default
    private List<UrlDTO.Response> additionalUrls = new ArrayList<>();

    @Builder.Default
    private List<AttributeDTO.Response> attributes = new ArrayList<>();

    @Builder.Default
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Summary {
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

    @Builder.Default
    private List<FundingDTO.Response> funding = new ArrayList<>();

    @Builder.Default
    private List<CostOptionDTO.Response> costOptions = new ArrayList<>();
    private ProgramDTO.Response program;

    @Builder.Default
    private List<RequiredDocumentDTO.Response> requiredDocuments = new ArrayList<>();

    @Builder.Default
    private List<ServiceAtLocationDTO.Response> serviceAtLocations = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Response> contacts = new ArrayList<>();

    @Builder.Default
    private List<UrlDTO.Response> additionalUrls = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Response> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Response> schedules = new ArrayList<>();

    @Builder.Default
    private List<ServiceAreaDTO.Response> serviceAreas = new ArrayList<>();

    @Builder.Default
    private List<LanguageDTO.Response> languages = new ArrayList<>();

    @Builder.Default
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }
}
