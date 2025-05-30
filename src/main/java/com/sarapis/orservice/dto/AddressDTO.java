package com.sarapis.orservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AddressDTO {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String id;
    private String locationId;
    private String attention;
    @JsonProperty("address_1")
    @NotBlank
    private String address1;
    @JsonProperty("address_2")
    private String address2;
    @NotBlank
    private String city;
    private String region;
    @NotBlank
    private String stateProvince;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String country;
    @NotBlank
    private String addressType;
    private List<AttributeDTO.Request> attributes;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {
    private String id;
    @JsonIgnore
    private String locationId;
    private String attention;
    @JsonProperty("address_1")
    private String address1;
    @JsonProperty("address_2")
    private String address2;
    private String city;
    private String region;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String addressType;
    private List<AttributeDTO.Response> attributes;
    private List<MetadataDTO.Response> metadata;
  }
}
