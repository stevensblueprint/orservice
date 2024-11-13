package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Service service;

  @ManyToOne
  private ServiceAtLocation serviceAtLocation;

  @Column(name = "valid_from")
  private LocalDate validFrom;

  @Column(name = "valid_to")
  private LocalDate validTo;

  @Column(name = "dtstart")
  private LocalDate dtStart;

  private int TimeZone;
  private LocalDate until;
  private int count;

  @Enumerated(EnumType.STRING)
  private WkSt wkst;

  @Enumerated(EnumType.STRING)
  private Freq freq;

  private int interval;
  private String byday;
  private String byweekno;
  private String bymonthday;
  private String byyearday;
  private String description;

  @Column(name = "opens_at")
  private LocalDate opensAt;

  @Column(name = "closes_at")
  private LocalDate closesAt;

  private String scheduleLink;
  private String attendingType;
  private String notes;
}
