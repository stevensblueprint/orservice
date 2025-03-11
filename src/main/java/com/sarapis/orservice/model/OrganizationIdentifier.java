package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
  private Organization organization;

  @Column(name = "identifier_scheme")
  private String identifierScheme;

  @Column(name = "identifier_type")
  private String identifierType;

  @Column(name = "identifier")
  private String identifier;
}
