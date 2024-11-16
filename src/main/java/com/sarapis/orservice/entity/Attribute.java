package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  // FK Property
  @Column(name = "link_id", nullable = false, unique = true)
  private String linkId;

  @Column(name = "link_type")
  private String linkType;

  @Column(name = "link_entity")
  private String linkEntity;

  @Column(name = "value")
  private String value;

  @OneToOne
  @JoinColumn(name = "taxonomy_term_id")
  private TaxonomyTerm taxonomyTerm;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;

  @Column(name = "label")
  private String label;
}
