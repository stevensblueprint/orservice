package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Location;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Language {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "phone_id")
  private Phone phone;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "note")
  private String note;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
