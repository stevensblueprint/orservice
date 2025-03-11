package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_at_location")
@Setter
@Getter
public class ServiceAtLocation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", insertable = false, updatable = false)
  private String id;

  @Column(name = "service_id")
  private String serviceId;
  private String description;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private List<Contact> contacts;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private List<Phone> phones;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_at_location_id", referencedColumnName = "id")
  private List<Schedule> schedules;
}
