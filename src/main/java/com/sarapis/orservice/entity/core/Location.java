package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "location_type")
  private LocationType locationType;

  @Column(name = "url")
  private String url;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

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
  private List<Language> languages;

  @OneToMany
  @JoinColumn(name = "location_id")
  private List<Address> addresses;

  @OneToMany
  @JoinColumn(name = "location_id")
  private List<Contact> contacts;

  @OneToMany
  @JoinColumn(name = "location_id")
  private List<Accessibility> accessibility;

  @OneToMany
  @JoinColumn(name = "location_id")
  private List<Phone> phones;

  @OneToMany
  @JoinColumn(name = "location_id")
  private List<Schedule> schedules;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
