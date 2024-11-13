package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization_identifier")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationIdentifier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Organization organization;

  @Column(name = "identifier_scheme")
  private String identifierScheme;

  @Column(name = "identifier_type")
  private String identifierType;
}
