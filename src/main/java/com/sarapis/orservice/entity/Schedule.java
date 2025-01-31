package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ScheduleDTO;
import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @Column(name = "id", nullable = false)
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
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "dtstart")
    private LocalDate dtStart;

    @Column(name = "timezone")
    private int timezone;

    @Column(name = "until")
    private LocalDate until;

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
    private LocalTime opensAt;

    @Column(name = "closes_at")
    private LocalTime closesAt;

    @Column(name = "schedule_link")
    private String scheduleLink;

    @Column(name = "attending_type")
    private String attendingType;

    @Column(name = "notes")
    private String notes;

    public ScheduleDTO toDTO() {
        return ScheduleDTO.builder()
                .id(this.id)
                .serviceId(this.service == null ? null : this.service.getId())
                .locationId(this.location == null ? null : this.location.getId())
                .serviceAtLocationId(this.serviceAtLocation == null ? null : this.serviceAtLocation.getId())
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .dtStart(this.dtStart)
                .timezone(this.timezone)
                .until(this.until)
                .count(this.count)
                .wkst(this.wkst)
                .freq(this.freq)
                .interval(this.interval)
                .byday(this.byday)
                .byweekno(this.byweekno)
                .bymonthday(this.bymonthday)
                .byyearday(this.byyearday)
                .description(this.description)
                .opensAt(this.opensAt)
                .closesAt(this.closesAt)
                .scheduleLink(this.scheduleLink)
                .attendingType(this.attendingType)
                .notes(this.notes)
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
