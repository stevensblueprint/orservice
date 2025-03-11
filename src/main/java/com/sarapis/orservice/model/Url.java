package com.sarapis.orservice.model;

import static com.sarapis.orservice.utils.MetadataUtils.URL_RESOURCE_TYPE;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;


@Entity
@Table(name = "url")
@Getter
@Setter
public class Url {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "label")
  private String label;

  @Column(name = "url", nullable = false)
  private String url;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne
  @JoinColumn(name = "service_id")
  private Service service;
}
