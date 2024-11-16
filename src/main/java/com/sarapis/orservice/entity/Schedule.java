package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "service_at_location_id")
  private ServiceAtLocation serviceAtLocation;

  @Column(name = "valid_from")
  private Date validFrom;

  @Column(name = "valid_to")
  private Date validTo;

  @Column(name = "dtstart")
  private Date dtStart;

  @Column(name = "timezone")
  private int timezone;

  @Column(name = "until")
  private Date until;

  @Column(name = "count")
  private int count;

  @Enumerated(EnumType.STRING)
  @Column(name = "wkst")
  private WkSt wkst;

  @Enumerated(EnumType.STRING)
  @Column(name = "freq")
  private Freq freq;

  @Column(name = "interval")
  private int interval;

  @Column(name = "byday")
  private String byday;

  @Column(name = "byweekno")
  private String byweekno;

  @Column(name = "bymonthday")
  private String bymonthday;

  @Column(name = "byyearday")
  private String byyearday;

  @Column(name = "description")
  private String description;

  @Column(name = "opens_at")
  private Time opensAt;

  @Column(name = "closes_at")
  private Time closesAt;

  @Column(name = "schedule_link")
  private String scheduleLink;

  @Column(name = "attending_type")
  private String attendingType;

  @Column(name = "notes")
  private String notes;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
