package com.sarapis.orservice.dto;

import com.sarapis.orservice.validator.ValidEmail;
import com.sarapis.orservice.validator.ValidUrl;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ServiceDTO {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
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
    private String accreditations;
    private String eligibilityDescription;
    private Integer minimumAge;
    private Integer maximumAge;
    private String assuredDate;

    @ValidEmail
    private String assurerEmail;

    private LocalDate lastModified;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
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
    private String accreditations;
    private String eligibilityDescription;
    private Integer minimumAge;
    private Integer maximumAge;
    private String assuredDate;
    private String assurerEmail;
    private LocalDate lastModified;
  }
}
