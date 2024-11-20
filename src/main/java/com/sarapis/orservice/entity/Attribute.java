package com.sarapis.orservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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

  private String linkId;
  private String linkType;
  private String linkEntity;
  private String value;

  @OneToOne
  @JoinColumn(name="taxonomy_term_id")
  private TaxonomyTerm taxonomyTerm;

  @OneToOne
  private Metadata metadata;

  private String label;
}
