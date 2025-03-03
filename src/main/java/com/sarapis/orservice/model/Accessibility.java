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
@Table(name = "accessibility")
@Getter
@Setter
public class Accessibility {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "location_id")
  private String locationId;

  @Column(name = "description")
  private String description;

  @Column(name = "details")
  private String details;

  @Column(name = "url")
  private String url;

  @OneToMany(mappedBy = "linkId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Attribute> attributes;

  @OneToMany(mappedBy = "resourceId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Metadata> metadata;
}
