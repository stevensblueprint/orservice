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
@Table(name = "language")
@Setter
@Getter
public class Language {
  @Id
  @Column(name = "id", nullable = false, insertable = false)
  private String id;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "location_id")
  private String locationId;

  @Column(name = "phone_id")
  private String phoneId;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "note")
  private String note;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "link_id", referencedColumnName = "id")
  private List<Attribute> attributes;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "resource_id", referencedColumnName = "id")
  private List<Metadata> metadata;

}
