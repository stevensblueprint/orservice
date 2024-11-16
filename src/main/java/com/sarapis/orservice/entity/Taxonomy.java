package com.sarapis.orservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "taxonomy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Taxonomy {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "uri")
  private String uri;

  @Column(name = "version")
  private String version;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
