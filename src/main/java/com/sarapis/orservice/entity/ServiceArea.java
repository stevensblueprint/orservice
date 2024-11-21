package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Service;
import com.sarapis.orservice.entity.core.ServiceAtLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "service_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceArea {
  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "id", nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @ManyToOne
  @JoinColumn(name = "service_at_location_id")
  private ServiceAtLocation serviceAtLocation;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "extent")
  private String extent;

  @Enumerated(EnumType.STRING)
  @Column(name = "extent_type")
  private ExtentType extentType;

  @Column(name = "uri")
  private String uri;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
