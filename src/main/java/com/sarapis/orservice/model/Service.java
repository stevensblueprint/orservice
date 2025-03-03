package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service")
@Getter
@Setter
public class Service {
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private String id;

  @NotBlank
  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @Column(name = "description")
  private String description;

  @Column(name = "url")
  private String url;

  @Column(name = "email")
  private String email;

  @NotBlank
  @Column(name = "status")
  private String status;

  @Column(name = "interpretation_services")
  private String interpretationServices;

  @Column(name = "application_process")
  private String applicationProcess;

  @Column(name = "fees_description")
  private String feesDescription;

  @Column(name = "accreditations")
  private String accreditations;

  @Column(name = "eligibility_description")
  private String eligibility_description;

  @Column(name = "minimum_age")
  private Integer minimumAge;

  @Column(name = "maximum_age")
  private Integer maximumAge;

  @Column(name = "assured_date")
  private String assuredDate;

  @Column(name = "assurer_email")
  private String assurerEmail;

  @Column(name = "licenses")
  private String licenses;

  @Column(name = "alert")
  private String alert;

  @Column(name = "last_modified")
  private LocalDate lastModified;
}
