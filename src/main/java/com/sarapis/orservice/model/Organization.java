package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.CONTACT_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.FUNDING_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.LOCATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_IDENTIFIER_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.ORGANIZATION_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.PHONE_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.PROGRAM_RESOURCE_TYPE;
import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "organization")
@Setter
@Getter
public class Organization {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
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

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Url> additionalWebsites;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Funding> funding;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Contact> contacts;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Phone> phones;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Program> programs;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<OrganizationIdentifier> organizationIdentifiers;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private List<Location> locations;
}
