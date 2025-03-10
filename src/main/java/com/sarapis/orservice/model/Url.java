package com.sarapis.orservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "url")
@Getter
@Setter
public class Url {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "label")
  private String label;

  @Column(name = "url", nullable = false)
  private String url;

  @Column(name = "organization_id")
  private String organizationId;

  @Column(name = "service_id")
  private String serviceId;
}
