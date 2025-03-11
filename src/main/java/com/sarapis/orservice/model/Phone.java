package com.sarapis.orservice.model;

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
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "phone")
@Getter
@Setter
public class Phone {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "location_id", referencedColumnName = "id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "service_id", referencedColumnName = "id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private Organization organization;

  @ManyToOne
  @JoinColumn(name = "contact_id", referencedColumnName = "id")
  private Contact contact;

  @ManyToOne
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private ServiceAtLocation serviceAtLocation;

  @NotBlank
  @Column(name = "number")
  private String number;

  @Column(name = "extension")
  private String extension;

  @Column(name = "type")
  private String type;

  @Column(name = "description")
  private String description;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "phone_id", referencedColumnName = "id")
  private List<Language> languages;
}
