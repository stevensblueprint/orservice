package com.sarapis.orservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "link_type")
@Getter
@Setter
public class LinkType {
  @Id
  @Column(name = "id", updatable = false, unique = true)
  private String id;

  @Column(name = "link_type")
  private String linkType;
}
