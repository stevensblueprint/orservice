package com.sarapis.orservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
public class Contact {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private Organization organization;

  @ManyToOne
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private ServiceAtLocation serviceAtLocation;

  @ManyToOne
  @JoinColumn(name = "location_id", referencedColumnName = "id")
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
  @JoinColumn(name = "contact_id", referencedColumnName = "id")
  private List<Phone> phones;
}
