package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.entity.Program;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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

  @ManyToOne
  private Organization organization;

  @ManyToOne
  private Program program;

  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  private String description;

  private String Url;

  private String email;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "interpretation_services")
  private String interpretationServices;

  @Column(name = "application_process")
  private String applicationProcess;

  @Column(name = "fees_description")
  private String feesDescription;

  @Column(name = "wait_time")
  private String waitTime;

  private String fees;

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

  private String licenses;

  private String alert;

  @Column(name = "last_modified")
  private LocalDate lastModified;

}
