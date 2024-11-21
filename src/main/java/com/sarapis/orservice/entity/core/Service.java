package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @Column(name = "description")
  private String description;

  @Column(name = "url")
  private String url;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<Url> additionUrls;

  @Column(name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Column(name = "interpretation_services")
  private String interpretationServices;

  @Column(name = "application_process")
  private String applicationProcess;

  @Column(name = "fees_description")
  private String feesDescription;

  // Deprecated
  @Column(name = "wait_time")
  private String waitTime;

  // Deprecated
  @Column(name = "fees")
  private String fees;

  @Column(name = "accreditations")
  private String accreditations;

  @Column(name = "eligibility_description")
  private String eligibilityDescription;

  @Column(name = "minimum_age")
  private int minimumAge;

  @Column(name = "maximum_age")
  private int maximumAge;

  @Column(name = "assured_date")
  private LocalDate assuredDate;

  @Column(name = "assurer_email")
  private String assurerEmail;

  // Deprecated
  @Column(name = "licenses")
  private String licenses;

  @Column(name = "alert")
  private String alert;

  @Column(name = "last_modified")
  private LocalDate lastModified;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<Phone> phones;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<Schedule> schedules;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<ServiceArea> serviceAreas;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<ServiceAtLocation> serviceAtLocations;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<Language> languages;

  @OneToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<Funding> funding;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<CostOption> costOptions;

  @OneToOne
  @JoinColumn(name = "program_id")
  private Program program;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<RequiredDocument> requiredDocuments;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<Contact> contacts;

  @OneToMany
  @JoinColumn(name = "service_id")
  private List<ServiceCapacity> capacities;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
