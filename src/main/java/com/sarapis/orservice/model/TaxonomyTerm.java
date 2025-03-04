package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taxonomy_term")
@Getter
@Setter
@RequiredArgsConstructor
public class TaxonomyTerm {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column
  private String language;

  @Column
  private String termUri;
}
