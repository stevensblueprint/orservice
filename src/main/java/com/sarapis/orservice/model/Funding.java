package com.sarapis.orservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "funding")
@Setter
@Getter
public class Funding {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "organization_id")
  private String organizationId;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "source")
  private String source;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "link_id", referencedColumnName = "id")
  private List<Attribute> attributes;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "resource_id", referencedColumnName = "id")
  private List<Metadata> metadata;
}
