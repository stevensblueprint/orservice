package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.Organization;
import com.sarapis.orservice.utils.IntegerUtils;
import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import com.sarapis.orservice.validator.ValidYear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotBlank;

import lombok.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class OrganizationDTO {
  @Getter
  @Setter
  @Builder
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

  public static final List<String> EXPORT_HEADER = Arrays.asList(
    "id",
    "name",
    "alternate_name",
    "description",
    "email",
    "uri",
    "tax_status",
    "tax_id",
    "year_incorporated",
    "legal_status"
  );

  public static List<String> toExport(Organization organization) {
    return Arrays.asList(
      organization.getId(),
      organization.getName(),
      organization.getAlternateName(),
      organization.getDescription(),
      organization.getEmail(),
      organization.getUri(),
      organization.getTaxStatus(),
      organization.getTaxId(),
      organization.getYearIncorporated() == null ? null : organization.getYearIncorporated().toString(),
      organization.getLegalStatus()
    );
  }

  public static List<OrganizationDTO.Request> csvToOrganizations(InputStream inputStream) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
         CSVParser csvParser = new CSVParser(fileReader,
                 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""))) {
      List<OrganizationDTO.Request> organizations = new ArrayList<>();
      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      for (CSVRecord csvRecord : csvRecords) {
        OrganizationDTO.Request organization = Request.builder()
          .id(csvRecord.get("id"))
          .name(csvRecord.get("name"))
          .description(csvRecord.get("description"))
          .email(csvRecord.get("email"))
          .uri(csvRecord.get("uri"))
          .taxStatus(csvRecord.get("tax_status"))
          .taxId(csvRecord.get("tax_id"))
          .yearIncorporated(IntegerUtils.parseIntOrNull(csvRecord.get("year_incorporated")))
          .legalStatus(csvRecord.get("legal_status"))
          .build();
        organizations.add(organization);
      }

      return organizations;
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
    }
  }
}
