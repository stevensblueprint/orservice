package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_at_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAtLocation {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "service_at_location_id")
    private List<ServiceArea> serviceAreas = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_at_location_id")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_at_location_id")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "service_at_location_id")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();
}
