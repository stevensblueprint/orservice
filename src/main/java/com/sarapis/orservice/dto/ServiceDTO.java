package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.Service;
import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import lombok.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceDTO {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    private List<FundingDTO.Response> funding = new ArrayList<>();
    private List<CostOptionDTO.Response> costOptions = new ArrayList<>();
    private ProgramDTO.Response program;
    private List<RequiredDocumentDTO.Response> requiredDocuments = new ArrayList<>();
    private List<ContactDTO.Response> contacts = new ArrayList<>();
    private List<UrlDTO.Response> additionalUrls = new ArrayList<>();
    private List<PhoneDTO.Response> phones = new ArrayList<>();
    private List<ScheduleDTO.Response> schedules = new ArrayList<>();
    private List<ServiceAreaDTO.Response> serviceAreas = new ArrayList<>();
    private List<LanguageDTO.Response> languages = new ArrayList<>();
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }

  public static final List<String> EXPORT_HEADER = Arrays.asList(
    "id",
    "organization_id",
    "name",
    "alternate_name",
    "description",
    "url",
    "email",
    "status",
    "interpretation_services",
    "application_process",
    "wait_time",
    "fees",
    "accreditations",
    "licenses"
  );

  public static List<String> toExport(Service service) {
    return Arrays.asList(
      service.getId(),
      service.getOrganization() == null ? null : service.getOrganization().getId(),
      service.getName(),
      service.getAlternateName(),
      service.getDescription(),
      service.getUrl(),
      service.getEmail(),
      service.getStatus(),
      service.getInterpretationServices(),
      service.getApplicationProcess(),
      service.getWaitTime(),
      service.getFees(),
      service.getAccreditations(),
      service.getLicenses()
    );
  }

  public static List<ServiceDTO.Request> csvToServices(InputStream inputStream) throws IOException {
    BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = new CSVParser(fileReader,
      CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));
    List<ServiceDTO.Request> services = new ArrayList<>();
    Iterable<CSVRecord> csvRecords = csvParser.getRecords();
    for (CSVRecord csvRecord : csvRecords) {
      ServiceDTO.Request service = Request.builder()
        .id(csvRecord.get("id"))
        .organization(OrganizationDTO.Request.builder().id(csvRecord.get("organization_id")).build())
        .program(ProgramDTO.Request.builder().id(csvRecord.get("program_id")).build())
        .name(csvRecord.get("name"))
        .alternateName(csvRecord.get("alternate_name"))
        .description(csvRecord.get("description"))
        .url(csvRecord.get("url"))
        .email(csvRecord.get("email"))
        .status(csvRecord.get("status"))
        .interpretationServices(csvRecord.get("interpretation_services"))
        .applicationProcess(csvRecord.get("application_process"))
        .waitTime(csvRecord.get("wait_time"))
        .fees(csvRecord.get("fees"))
        .accreditations(csvRecord.get("accreditations"))
        .licenses(csvRecord.get("licenses"))
        .build();
      services.add(service);
    }

    return services;
  }
}
