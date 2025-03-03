package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization")
@Setter
@Getter
public class Organization {
  @Id
  @Column(name = "id", insertable = false, updatable = false)
  private String id;
}
