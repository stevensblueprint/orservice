package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Setter
@Getter
public class Organization {
  @Id
  @Column(name = "id", insertable = false, updatable = false)
  private String id;

  @NotBlank
  @Column(name = "name")
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @NotBlank
  @Column(name = "description")
  private String description;

  @Column(name = "email")
  private String email;

  @Column(name = "website")
  private String website;

  @Column(name = "tax_status")
  private String taxStatus;

  @Column(name = "tax_id")
  private String taxId;

  @Column(name = "year_incorporated")
  private Integer yearIncorporated;

  @Column(name = "legal_status")
  private String legalStatus;

  @Column(name = "logo")
  private String logo;

  @Column(name = "uri")
  private String uri;
}
