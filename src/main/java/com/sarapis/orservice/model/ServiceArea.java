package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_area")
@Getter
@Setter
public class ServiceArea {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "extent")
  private String extent;

  @Column(name = "extent_type")
  private String extentType;

  @Column(name = "uri")
  private String uri;

  @OneToMany(mappedBy = "linkId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Attribute> attributes;

  @OneToMany(mappedBy = "resourceId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Metadata> metadata;
}
