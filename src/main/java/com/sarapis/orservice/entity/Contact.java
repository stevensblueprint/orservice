package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "contact")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "service_at_location_id")
  private ServiceAtLocation serviceAtLocation;

  @ManyToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @Column(name = "name")
  private String name;

  @Column(name = "title")
  private String title;

  @Column(name = "department")
  private String department;

  @Column(name = "email")
  private String email;

  @OneToMany
  @JoinColumn(name = "contact_id")
  private List<Phone> phones;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
