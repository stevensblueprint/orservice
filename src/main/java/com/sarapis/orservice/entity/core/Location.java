package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false)
    private LocationType locationType;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "description")
    private String description;

    @Column(name = "transportation")
    private String transportation;

    @Column(name = "latitude")
    private int latitude;

    @Column(name = "longitude")
    private int longitude;

    @Column(name = "external_identifier")
    private String externalIdentifier;

    @Column(name = "external_identifier_type")
    private String externalIdentifierType;

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Language> languages = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Address> addresses = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Accessibility> accessibility = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Phone> phones = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "link_id")
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_id")
    private List<Metadata> metadata = new ArrayList<>();
}
