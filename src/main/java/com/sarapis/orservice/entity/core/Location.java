package com.sarapis.orservice.entity.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "location_type")
  @Enumerated(EnumType.STRING)
  private LocationType locationType;

  @Column(name = "url")
  private String Url;

  @ManyToOne
  private Organization organization;

  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  private String description;

  private String transportation;

  private int latitude;

  private int longitude;

  @Column(name = "external_identifier")
  private String externalIdentifier;

  @Column(name = "external_identifier_type")
  private String externalIdentifierType;

}
