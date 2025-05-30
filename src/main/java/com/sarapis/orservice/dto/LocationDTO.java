package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sarapis.orservice.model.Location;
import com.sarapis.orservice.utils.IntegerUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class LocationDTO {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;

    private String locationType;

    private String url;

    private String organizationId;

    private String name;

    private String alternateName;

    private String description;

    private String transportation;

    private Integer latitude;

    private Integer longitude;

    private String externalIdentifier;

    private String externalIdentifierType;

    @Builder.Default
    private List<LanguageDTO.Request> languages = new ArrayList<>();

    @Builder.Default
    private List<AddressDTO.Request> addresses = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Request> contacts = new ArrayList<>();

    @Builder.Default
    private List<AccessibilityDTO.Request> accessibility = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Request> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Request> schedules = new ArrayList<>();

    @Builder.Default
    private List<AttributeDTO.Request> attributes = new ArrayList<>();
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;

    private String locationType;

    private String url;

    @JsonIgnore
    private String organizationId;

    private String name;

    private String alternateName;

    private String description;

    private String transportation;

    private Integer latitude;

    private Integer longitude;

    private String externalIdentifier;

    private String externalIdentifierType;

    @Builder.Default
    private List<LanguageDTO.Response> languages = new ArrayList<>();

    @Builder.Default
    private List<AddressDTO.Response> addresses = new ArrayList<>();

    @Builder.Default
    private List<ContactDTO.Response> contacts = new ArrayList<>();

    @Builder.Default
    private List<AccessibilityDTO.Response> accessibility = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Response> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Response> schedules = new ArrayList<>();

    @Builder.Default
    private List<AttributeDTO.Response> attributes = new ArrayList<>();

    @Builder.Default
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }

  public static final List<String> EXPORT_HEADER = Arrays.asList(
    "id",
    "organization_id",
    "location_type",
    "name",
    "alternate_name",
    "description",
    "transportation",
    "latitude",
    "longitude"
  );

  public static List<String> toExport(Location location) {
    return Arrays.asList(
      location.getId(),
      location.getOrganization() == null ? null : location.getOrganization().getId(),
      location.getLocationType().toString(),
      location.getName(),
      location.getAlternateName(),
      location.getDescription(),
      location.getTransportation(),
      location.getLatitude().toString(),
      location.getLongitude().toString()
    );
  }

  public static List<LocationDTO.Request> csvToLocations(InputStream inputStream) throws IOException {
    BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = new CSVParser(fileReader,
      CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));
    List<LocationDTO.Request> locations = new ArrayList<>();
    Iterable<CSVRecord> csvRecords = csvParser.getRecords();
    for (CSVRecord csvRecord : csvRecords) {
      LocationDTO.Request location = Request.builder()
        .id(csvRecord.get("id"))
        .organizationId(csvRecord.get("organization_id"))
        .locationType(csvRecord.get("location_type"))
        .name(csvRecord.get("name"))
        .alternateName(csvRecord.get("alternate_name"))
        .description(csvRecord.get("description"))
        .transportation(csvRecord.get("transportation"))
        .latitude(IntegerUtils.parseIntOrNull(csvRecord.get("latitude")))
        .longitude(IntegerUtils.parseIntOrNull(csvRecord.get("longitude")))
        .build();
      locations.add(location);
    }

    return locations;
  }
}
