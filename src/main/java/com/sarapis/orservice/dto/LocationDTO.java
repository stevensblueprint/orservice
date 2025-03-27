package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int latitude;
    private int longitude;
    private String externalIdentifier;
    private String externalIdentifierType;
    private List<LanguageDTO.Request> languages;
    private List<AddressDTO.Request> addresses;
    private List<ContactDTO.Request> contacts;
    private List<AccessibilityDTO.Request> accessibility;
    private List<PhoneDTO.Request> phones;
    private List<ScheduleDTO.Request> schedules;
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
    private int latitude;
    private int longitude;
    private String externalIdentifier;
    private String externalIdentifierType;
    private List<LanguageDTO.Response> languages;
    private List<AddressDTO.Response> addresses;
    private List<ContactDTO.Response> contacts;
    private List<AccessibilityDTO.Response> accessibility;
    private List<PhoneDTO.Response> phones;
    private List<ScheduleDTO.Response> schedules;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
