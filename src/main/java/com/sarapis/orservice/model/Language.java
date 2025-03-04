package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "language")
@Setter
@Getter
public class Language {
  @Id
  @Column(name = "id", nullable = false, insertable = false)
  private String id;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "location_id")
  private String locationId;

  @Column(name = "phone_id")
  private String phoneId;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "note")
  private String note;
}
