package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "location_id")
  private String locationId;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "organization_id")
  private String organizationId;

  @Column(name = "contact_id")
  private String contact_id;

  @Column(name = "service_at_location_id")
  private String serviceAtLocationId;

  @NotBlank
  @Column(name = "number")
  private String number;

  @Column(name = "extension")
  private String extension;

  @Column(name = "type")
  private String type;

  @Column(name = "description")
  private String description;

  // TODO: Add languages entity

  @OneToMany(mappedBy = "linkId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Attribute> attributes;

  @OneToMany(mappedBy = "resourceId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Metadata> metadata;

}
