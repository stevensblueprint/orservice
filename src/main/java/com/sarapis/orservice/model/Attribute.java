package com.sarapis.orservice.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attribute")
@Getter
@Setter
public class Attribute {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "link_id", nullable = false)
  private String linkId;

  @Column(name = "link_type")
  private String linkType;

  @Column(name = "link_entity", nullable = false)
  private String linkEntity;

  @Column(name = "value")
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxonomy_term_id", insertable = false, updatable = false)
  private TaxonomyTerm taxonomyTerm;

  @PrePersist
  public void prePersist() {
    if (this.getId() == null) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}