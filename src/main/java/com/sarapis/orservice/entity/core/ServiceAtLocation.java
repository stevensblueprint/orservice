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

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "description")
  private String description;

  @OneToMany
  @JoinColumn(name = "service_at_location_id")
  private List<ServiceArea> serviceAreas;

  @OneToMany
  @JoinColumn(name = "service_at_location_id")
  private List<Contact> contacts;

  @OneToMany
  @JoinColumn(name = "service_at_location_id")
  private List<Phone> phones;

  @OneToMany
  @JoinColumn(name = "service_at_location_id")
  private List<Schedule> schedules;

  @OneToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
