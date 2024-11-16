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

import java.util.List;

@Entity
@Table(name = "phone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "contact_id")
  private Contact contact;

  @ManyToOne
  @JoinColumn(name = "service_at_location_id")
  private ServiceAtLocation serviceAtLocation;

  @Column(name = "number")
  private String number;

  @Column(name = "extension")
  private String extension;

  @Column(name = "type")
  private String type;

  @Column(name = "description")
  private String description;

  @OneToMany
  @JoinColumn(name = "phone_id")
  private List<Language> languages;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
