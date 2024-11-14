package com.sarapis.orservice.dto;

import com.sarapis.orservice.entity.Program;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ServiceDTO {
  private String id;
  private OrganizationDTO organization;
  private Program program;
  private String name;
  private String alternateName;
  private String url;
  private String email;
  private Status status;
  private String interpretationServices;
  private String applicationProcess;
  private String feesDescription;
  private String waitTime;
  private String fees;
  private String accreditations;
  private String eligibilityDescription;
  private int minimumAge;
  private int maximumAge;
  private String assuredDate;
  private String assurerEmail;
  private String licenses;
  private String alert;
  private String lastModified;
}
