package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Organization;
import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Url {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @Column(name = "label")
  private String label;

  @Column(name = "url")
  private String url;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
