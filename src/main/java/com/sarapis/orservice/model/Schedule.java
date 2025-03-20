package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.SCHEDULE_RESOURCE_TYPE;

import com.sarapis.orservice.repository.MetadataRepository;
import com.sarapis.orservice.utils.MetadataType;
import com.sarapis.orservice.utils.MetadataUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule {
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "service_at_location_id")
  private ServiceAtLocation serviceAtLocation;

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

  public void setMetadata(MetadataRepository metadataRepository, String updatedBy) {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
    metadataRepository.saveAll(MetadataUtils.createMetadata(
        this,
        this,
        this.getId(),
        SCHEDULE_RESOURCE_TYPE,
        MetadataType.CREATE,
        updatedBy
    ));
  }
}
