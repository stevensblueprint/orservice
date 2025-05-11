package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.LocationType;
import com.sarapis.orservice.model.Schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sarapis.orservice.model.ServiceAtLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ServiceAtLocationDTO {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;

    private ServiceDTO.Request service;

    private String description;

    private LocationDTO.Request location;

    @Builder.Default
    private List<ContactDTO.Request> contacts = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Request> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Request> schedules = new ArrayList<>();

    @Builder.Default
    private List<ServiceAreaDTO.Request> serviceAreas = new ArrayList<>();
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;

    private String description;

    private ServiceDTO.Summary service;

    private LocationDTO.Response location;

    @Builder.Default
    private List<ContactDTO.Response> contacts = new ArrayList<>();

    @Builder.Default
    private List<PhoneDTO.Response> phones = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDTO.Response> schedules = new ArrayList<>();

    @Builder.Default
    private List<ServiceAreaDTO.Response> serviceAreas = new ArrayList<>();

    @Builder.Default
    private List<AttributeDTO.Response> attributes = new ArrayList<>();

    @Builder.Default
    private List<MetadataDTO.Response> metadata = new ArrayList<>();
  }

  public static final List<String> EXPORT_HEADER = Arrays.asList(
    "id",
    "service_id",
    "location_id",
    "description"
  );

  public static List<String> toExport(ServiceAtLocation serviceAtLocation) {
    return Arrays.asList(
      serviceAtLocation.getId(),
      serviceAtLocation.getService() == null ? null : serviceAtLocation.getService().getId(),
      serviceAtLocation.getLocation() == null ? null : serviceAtLocation.getLocation().getId(),
      serviceAtLocation.getDescription()
    );
  }

  public static List<ServiceAtLocationDTO.Request> csvToServiceAtLocations(InputStream inputStream) throws IOException {
    BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = new CSVParser(fileReader,
      CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));
    List<ServiceAtLocationDTO.Request> serviceAtLocations = new ArrayList<>();
    Iterable<CSVRecord> csvRecords = csvParser.getRecords();
    for (CSVRecord csvRecord : csvRecords) {
      ServiceAtLocationDTO.Request serviceAtLocation = Request.builder()
        .id(csvRecord.get("id"))
        .service(ServiceDTO.Request.builder().id(csvRecord.get("service_id")).build())
        .location(LocationDTO.Request.builder()
          .id(csvRecord.get("location_id"))
          .locationType(LocationType.physical.name()) // temporarily set to avoid null constraint
          .build())
        .description(csvRecord.get("description"))
        .build();
      serviceAtLocations.add(serviceAtLocation);
    }

    return serviceAtLocations;
  }
}
