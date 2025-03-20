package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taxonomy")
@Getter
@Setter
@RequiredArgsConstructor
public class Taxonomy {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @NotNull
  @Size(max = 255)
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "version")
  private String version;
}
