package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private String id;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "location_id")
  private String locationId;

  @Column(name = "service_at_location_id")
  private String serviceAtLocationId;

  @Column(name = "valid_from")
  private String validFrom;

  @Column(name = "valid_to")
  private String validTo;

  @Column(name = "dtstart")
  private String dtStart;

  @Column(name = "timezone")
  private Integer timezone;

  @Column(name = "until")
  private LocalDate until;

  @Column(name = "count")
  private Integer count;

  @Enumerated(EnumType.STRING)
  @Column(name = "wkst")
  private WeekStart wkSt;

  @Column(name = "freq")
  private String freq;

  @Column(name = "interval")
  private Integer interval;

  @Column(name = "byday")
  private String byDay;

  @Column(name = "byweekno")
  private String byWeekNo;

  @Column(name = "bymonthday")
  private String byMonthDay;

  @Column(name = "byyearday")
  private String byYearDay;

  @Column(name = "description")
  private String description;

  @Column(name = "opens_at")
  private String opensAt;

  @Column(name = "closes_at")
  private String closesAt;

  @Column(name = "schedule_link")
  private String scheduleLink;

  @Column(name = "attending_type")
  private String attendingType;

  @Column(name = "notes")
  private String notes;
}
