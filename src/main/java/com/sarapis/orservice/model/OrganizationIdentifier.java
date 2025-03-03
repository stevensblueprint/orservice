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
@Table(name = "organization_identifier")
@Setter
@Getter
public class OrganizationIdentifier {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "organization_id")
  private String organizationId;

  @Column(name = "identifier_scheme")
  private String identifierScheme;

  @Column(name = "identifier_type")
  private String identifierType;

  @Column(name = "identifier")
  private String identifier;

  @OneToMany(mappedBy = "linkId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Attribute> attributes;

  @OneToMany(mappedBy = "resourceId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Metadata> metadata;
}
