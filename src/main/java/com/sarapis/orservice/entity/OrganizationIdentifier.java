package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "organization_identifier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationIdentifier {
  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "id", nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @Column(name = "identifier_scheme")
  private String identifierScheme;

  @Column(name = "identifier_type", nullable = false)
  private String identifierType;

  @Column(name = "identifier", nullable = false)
  private String identifier;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
