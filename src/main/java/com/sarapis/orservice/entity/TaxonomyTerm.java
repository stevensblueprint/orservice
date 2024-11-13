package com.sarapis.orservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taxonomy_term")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaxonomyTerm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;
  private String name;
  private String description;

  @OneToOne
  private TaxonomyTerm parent;

  @Column(name = "taxonomy")
  private String taxonomyName;

  @ManyToOne
  private Taxonomy taxonomy;
  private String language;

  @Column(name = "term_uri")
  private String termUri;

}
