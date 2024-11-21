package com.sarapis.orservice.entity.core;

import com.sarapis.orservice.dto.OrganizationDTO;
import com.sarapis.orservice.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "organization")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {
  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "alternate_name")
  private String alternateName;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "email")
  private String email;

  @Column(name = "website")
  private String website;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<Url> additionalWebsites;

  // Deprecated
  @Column(name = "tax_status")
  private String taxStatus;

  // Deprecated
  @Column(name = "tax_id")
  private String taxId;

  @Column(name = "year_incorporated")
  private int yearIncorporated;

  @Column(name = "legal_status")
  private String legalStatus;

  @Column(name = "logo")
  private String logo;

  @Column(name = "uri")
  private String uri;

  @OneToOne
  @JoinColumn(name = "parent_organization_id")
  private Organization parentOrganization;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<Funding> funding;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<Contact> contacts;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<Phone> phones;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<Location> locations;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<Program> programs;

  @OneToMany
  @JoinColumn(name = "organization_id")
  private List<OrganizationIdentifier> organizationIdentifiers;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;

  public OrganizationDTO toDTO() {
    return OrganizationDTO.builder()
       .id(id)
       .name(name)
       .alternateName(alternateName)
       .description(description)
       .email(email)
       .website(website)
       .additionalWebsites(additionalWebsites)
       .yearIncorporated(yearIncorporated)
       .legalStatus(legalStatus)
       .logo(logo)
       .uri(uri)
       .parentOrganization(parentOrganization)
       .build();
  }
}
