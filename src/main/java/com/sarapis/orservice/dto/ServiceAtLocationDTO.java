package com.sarapis.orservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.model.Schedule;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<ContactDTO.Request> contacts;
    private List<PhoneDTO.Request> phones;
    private List<ScheduleDTO.Request> schedules;
    private List<ServiceAreaDTO.Request> serviceAreas;
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
    private List<ContactDTO.Response> contacts;
    private List<PhoneDTO.Response> phones;
    private List<ScheduleDTO.Response> schedules;
    private List<ServiceAreaDTO.Response> serviceAreas;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
