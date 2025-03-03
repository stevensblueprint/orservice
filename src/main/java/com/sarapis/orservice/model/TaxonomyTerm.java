package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString.Exclude;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", insertable = false, updatable = false)
  @Exclude
  private TaxonomyTerm parent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxonomy_id", nullable = false)
  private Taxonomy taxonomy;

  @Column
  private String language;

  @Column
  private String termUri;

  @OneToMany(mappedBy = "resourceId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Exclude
  private List<Metadata> metadata;

}
