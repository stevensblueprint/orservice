package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "taxonomy_term")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaxonomyTerm {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @OneToOne
  @JoinColumn(name = "parent_id")
  private TaxonomyTerm parent;

  @Column(name = "taxonomy")
  private String taxonomy;

  @OneToOne
  @JoinColumn(name = "taxonomy_detail")
  private Taxonomy taxonomyDetail;

  @Column(name = "language")
  private String language;

  @Column(name = "taxonomy_id", nullable = false, unique = true)
  private String taxonomyId;

  @Column(name = "term_uri")
  private String termUri;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
