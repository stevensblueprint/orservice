package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sarapis.orservice.dto.MetadataDTO.Response;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ScheduleDTO {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;
    private String serviceId;
    private String locationId;
    private String serviceAtLocationId;
    private String validFrom;
    private String validTo;
    @JsonProperty("dtstart")
    private String dtStart;
    private Integer timezone;
    private String until;
    private Integer count;
    @JsonProperty("wkst")
    private String wkSt;
    private String freq;
    private Integer interval;
    @JsonProperty("byday")
    private String byDay;
    @JsonProperty("byweekno")
    private String byWeekNo;
    @JsonProperty("bymonthday")
    private String byMonthDay;
    @JsonProperty("byyearday")
    private String byYearDay;
    private String description;
    private String opensAt;
    private String closesAt;
    private String scheduleLink;
    private String attendingType;
    private String notes;
    private List<AttributeDTO.Request> attributes;
  }

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    @JsonIgnore
    private String serviceId;
    @JsonIgnore
    private String locationId;
    @JsonIgnore
    private String serviceAtLocationId;
    private String validFrom;
    private String validTo;
    @JsonProperty("dtstart")
    private String dtStart;
    private Integer timezone;
    private String until;
    private Integer count;
    @JsonProperty("wkst")
    private String wkSt;
    private String freq;
    private Integer interval;
    @JsonProperty("byday")
    private String byDay;
    @JsonProperty("byweekno")
    private String byWeekNo;
    @JsonProperty("bymonthday")
    private String byMonthDay;
    @JsonProperty("byyearday")
    private String byYearDay;
    private String description;
    private String opensAt;
    private String closesAt;
    private String scheduleLink;
    private String attendingType;
    private String notes;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
