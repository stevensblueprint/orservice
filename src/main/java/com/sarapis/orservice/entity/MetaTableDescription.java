package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "meta_table_description")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaTableDescription {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "language")
  private String language;

  @Column(name = "character_set")
  private String characterSet;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
