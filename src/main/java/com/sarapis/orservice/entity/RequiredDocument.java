package com.sarapis.orservice.entity;

import com.sarapis.orservice.entity.core.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Table(name = "required_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequiredDocument {
  @Id
  @GeneratedValue
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "document")
  private String document;

  @Column(name = "uri")
  private String uri;

  @OneToMany
  @JoinColumn(name = "link_id")
  private List<Attribute> attributes;

  @OneToMany
  @JoinColumn(name = "resource_id")
  private List<Metadata> metadata;
}
