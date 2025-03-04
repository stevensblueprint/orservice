package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Phone> phones;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Schedule> schedules;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<ServiceArea> serviceAreas;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<ServiceAtLocation> serviceAtLocations;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Language> languages;

  @OneToOne
  private Organization organization;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Funding> funding;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<CostOption> costOptions;

  @OneToOne
  private Program program;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<RequiredDocument> requiredDocuments;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private List<Contact> contacts;
}
