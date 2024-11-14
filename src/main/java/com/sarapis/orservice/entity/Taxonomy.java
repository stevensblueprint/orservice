package com.sarapis.orservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "taxonomy")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Taxonomy {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  private String name;

  private String description;

  private String uri;

  private String version;
}
