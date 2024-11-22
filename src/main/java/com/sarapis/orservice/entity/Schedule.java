package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

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
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();
}
